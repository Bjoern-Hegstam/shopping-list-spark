package com.bhegstam.shoppinglist;

import com.bhegstam.shoppinglist.application.ItemTypeApplication;
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
import com.bhegstam.shoppinglist.port.rest.ItemTypeResource;
import com.bhegstam.shoppinglist.port.rest.ShoppingListResource;
import com.bhegstam.shoppinglist.port.rest.UserResource;
import com.bhegstam.shoppinglist.port.rest.auth.AuthResource;
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
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
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
        bootstrap.addBundle(new AssetsBundle("/public", "/", "index.html")); // TODO: Ensure gzipped
    }

    @Override
    public void run(ShoppingListApplicationConfiguration config, Environment environment) {
        RepositoryFactory repositoryFactory = new RepositoryFactory(environment, config.getDataSourceFactory());

        UserRepository userRepository = repositoryFactory.createUserRepository();
        ItemTypeRepository itemTypeRepository = repositoryFactory.createItemTypeRepository();
        ShoppingListRepository shoppingListRepository = repositoryFactory.createShoppingListRepository();

        configureAuth(config, environment, userRepository);

        environment.jersey().register(new AuthResource(config.getJwtTokenSecret()));
        environment.jersey().register(new ItemTypeResource(new ItemTypeApplication(itemTypeRepository)));
        environment.jersey().register(new ShoppingListResource(
                new com.bhegstam.shoppinglist.application.ShoppingListApplication(
                        shoppingListRepository,
                        itemTypeRepository
                )
        ));
        environment.jersey().register(new UserResource(new UserApplication(userRepository)));

        configureCors(environment);
    }

    private void configureCors(Environment environment) {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Authorization,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
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
                .setAuthenticator(new JwtTokenAuthenticator(new UserApplication(userRepository)))
                .setAuthorizer(userRoleAuthorizer)
                .setPrefix("Bearer")
                .buildAuthFilter();

        List<AuthFilter> authFilters = asList(tokenAuthFilter, basicAuthFilter);
        environment.jersey().register(new AuthDynamicFeature(new ChainedAuthFilter(authFilters)));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
}
