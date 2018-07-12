package com.bhegstam;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.configuration.auth.BasicAuthenticator;
import com.bhegstam.shoppinglist.configuration.auth.RoleAuthorizer;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.shoppinglist.persistence.JdbiItemTypeRepository;
import com.bhegstam.shoppinglist.persistence.JdbiShoppingListRepository;
import com.bhegstam.shoppinglist.persistence.JdbiUserRepository;
import com.bhegstam.shoppinglist.port.rest.login.LoginResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

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
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new FlywayBundle<ShoppingListApplicationConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(ShoppingListApplicationConfiguration config) {
                return config.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(new AssetsBundle("/public")); // TODO: Ensure gzipped
    }

    @Override
    public void run(ShoppingListApplicationConfiguration config, Environment environment) {
        DBIFactory factory = new DBIFactory();
        DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "postgresql");

        UserRepository userRepository = jdbi.onDemand(JdbiUserRepository.class);
        ItemTypeRepository itemTypeRepository = jdbi.onDemand(JdbiItemTypeRepository.class);
        ShoppingListRepository shoppingListRepository = jdbi.onDemand(JdbiShoppingListRepository.class);

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new BasicAuthenticator(new UserApplication(userRepository)))
                .setAuthorizer(new RoleAuthorizer())
                .setPrefix("Basic")
                .setRealm("SUPER SECRET STUFF") // TODO: Change
                .buildAuthFilter()));
        // TODO: Add JwtTokenAuthenticator for prefix Bearer
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        // TODO: Link to example https://github.com/dropwizard/dropwizard/blob/master/dropwizard-example/src/main/java/com/example/helloworld/HelloWorldApplication.java

        environment.jersey().register(new LoginResource());
    }

//    public ShoppingListApplication(ShoppingListApplicationConfiguration conf) {
//        super(
//                List.of(
//                        new IndexController(),
//                        new LoginResource(
//                                new UserApplication(conf.getJdbi().onDemand(JdbiUserRepository.class))
//                        )
//                ),
//                asList(
//                        new ItemTypeApiController(
//                                new ItemTypeApplication(conf.getJdbi().onDemand(JdbiItemTypeRepository.class))
//                        ),
//                        new ShoppingListApiController(
//                                new com.bhegstam.shoppinglist.application.ShoppingListApplication(
//                                        conf.getJdbi().onDemand(JdbiShoppingListRepository.class),
//                                        conf.getJdbi().onDemand(JdbiItemTypeRepository.class)
//                                )
//                        ),
//                        new UserApiController(new UserApplication(conf.getJdbi().onDemand(JdbiUserRepository.class)))
//                ),
//                true,
//                true
//        );
//    }
}
