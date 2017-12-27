package com.bhegstam;

import com.bhegstam.configuration.ApplicationConfiguration;
import com.bhegstam.configuration.ConfigurationModule;
import com.bhegstam.login.LoginController;
import com.bhegstam.shoppinglist.ShoppingListModule;
import com.bhegstam.shoppinglist.controller.ShoppingListApiController;
import com.bhegstam.shoppinglist.controller.ShoppingListController;
import com.bhegstam.user.UserModule;
import com.bhegstam.user.controller.UserAdministrationController;
import com.bhegstam.user.controller.UserApiController;
import com.bhegstam.user.controller.UserRegistrationController;
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
