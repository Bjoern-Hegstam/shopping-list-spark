package com.bhegstam;

import com.bhegstam.configuration.ApplicationConfiguration;
import com.bhegstam.configuration.ConfigurationModule;
import com.bhegstam.login.LoginController;
import com.bhegstam.shoppinglist.ShoppingListController;
import com.bhegstam.shoppinglist.ShoppingListModule;
import com.bhegstam.shoppinglist.api.ShoppingListApiController;
import com.bhegstam.user.UserAdministrationController;
import com.bhegstam.user.UserModule;
import com.bhegstam.user.UserRegistrationController;
import com.bhegstam.user.api.UserApiController;
import com.bhegstam.webutil.webapp.ApplicationBase;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import spark.Service;

import static java.util.Arrays.asList;

public class Application extends ApplicationBase {

    private final ApplicationConfiguration configuration;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ConfigurationModule(),
                new ShoppingListModule(),
                new UserModule()
        );

        injector
             .getInstance(Application.class)
             .init();
    }

    @Inject
    public Application(
            ApplicationConfiguration configuration,
            IndexController indexController,
            LoginController loginController,
            ShoppingListController shoppingListController,
            ShoppingListApiController shoppingListApiController,
            UserRegistrationController userRegistrationController,
            UserAdministrationController userAdministrationController,
            UserApiController userApiController
    ) {
        super(
                asList(
                        indexController,
                        loginController,
                        shoppingListController,
                        userRegistrationController,
                        userAdministrationController
                ),
                asList(
                        shoppingListApiController,
                        userApiController
                ),
                true
        );

        this.configuration = configuration;
    }

    @Override
    protected void configureServer(Service http) {
        http.port(configuration.getServer().getPort());

        http.staticFiles.location("/public");
        http.staticFiles.expireTime(600);
    }

}
