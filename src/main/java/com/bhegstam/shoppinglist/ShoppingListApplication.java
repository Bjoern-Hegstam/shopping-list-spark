package com.bhegstam.shoppinglist;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.configuration.DbMigrationBundle;
import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.configuration.auth.BasicAuthenticator;
import com.bhegstam.shoppinglist.configuration.auth.JwtTokenAuthenticator;
import com.bhegstam.shoppinglist.configuration.auth.UserRoleAuthorizer;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.shoppinglist.port.persistence.RepositoryFactory;
import com.bhegstam.shoppinglist.port.rest.login.AuthResource;
import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import java.util.List;

import static java.util.Arrays.asList;

public class ShoppingListApplication extends Application<ShoppingListApplicationConfiguration> {
    public static void main(String[] args) throws Exception {
        new ShoppingListApplication().run(args);
    }

    @Override
    public String getName() {
        return "shopping-list";
    }

    @Override
    public void initialize(Bootstrap<ShoppingListApplicationConfiguration> bootstrap) {
        bootstrap.setObjectMapper(Jackson.newObjectMapper());
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new DbMigrationBundle());
        bootstrap.addBundle(new AssetsBundle("/public")); // TODO: Ensure gzipped
    }

    @Override
    public void run(ShoppingListApplicationConfiguration config, Environment environment) {
        RepositoryFactory repositoryFactory = new RepositoryFactory(environment, config.getDataSourceFactory());

        UserRepository userRepository = repositoryFactory.createUserRepository();
        ItemTypeRepository itemTypeRepository = repositoryFactory.createItemTypeRepository();
        ShoppingListRepository shoppingListRepository = repositoryFactory.createShoppingListRepository();

        configureAuth(config, environment, userRepository);

        environment.jersey().register(new AuthResource(config.getJwtTokenSecret()));
    }

    private void configureAuth(ShoppingListApplicationConfiguration config, Environment environment, UserRepository userRepository) {
        UserRoleAuthorizer userRoleAuthorizer = new UserRoleAuthorizer();

        BasicCredentialAuthFilter<User> basicAuthFilter = new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new BasicAuthenticator(new UserApplication(userRepository)))
                .setAuthorizer(userRoleAuthorizer)
                .setPrefix("Basic")
                .buildAuthFilter();

        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30)
                .setRequireExpirationTime()
                .setRequireSubject()
                .setRequireIssuedAt()
                .setVerificationKey(new HmacKey(config.getJwtTokenSecret()))
                .setRelaxVerificationKeyValidation()
                .build();

        JwtAuthFilter<User> tokenAuthFilter = new JwtAuthFilter.Builder<User>()
                .setJwtConsumer(consumer)
                .setAuthenticator(new JwtTokenAuthenticator())
                .setAuthorizer(userRoleAuthorizer)
                .setPrefix("Bearer")
                .buildAuthFilter();

        List<AuthFilter> authFilters = asList(tokenAuthFilter, basicAuthFilter);
        environment.jersey().register(new AuthDynamicFeature(new ChainedAuthFilter(authFilters)));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }

//                        new ItemTypeApiController( new ItemTypeApplication(itemTypeRepository) ),
//                        new ShoppingListApiController(
//                                new com.bhegstam.shoppinglist.application.ShoppingListApplication(
//                                        shoppingListRepository,
//                                        itemTypeRepository
//                                )
//                        ),
//                        new UserApiController(new UserApplication(userRepository))
}
