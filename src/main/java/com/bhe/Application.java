package com.bhe;

import com.bhe.admin.UserAdministrationController;
import com.bhe.configuration.ConfigurationModule;
import com.bhe.login.LoginController;
import com.bhe.user.*;
import com.bhe.util.Filters;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import spark.Service;

import static spark.Service.ignite;

public class Application {

    private final IndexController indexController;
    private final LoginController loginController;
    private final UserRegistrationController userRegistrationController;
    private final UserAdministrationController userAdministrationController;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ConfigurationModule(),
                new UserModule()
        );

        injector
             .getInstance(Application.class)
             .init();
    }

    @Inject
    public Application(
            IndexController indexController,
            LoginController loginController,
            UserRegistrationController userRegistrationController,
            UserAdministrationController userAdministrationController
    ) {
        this.indexController = indexController;
        this.loginController = loginController;
        this.userRegistrationController = userRegistrationController;
        this.userAdministrationController = userAdministrationController;
    }

    private void init() {
        Service http = ignite();
        configureServer(http);
        configureRoutes(http);
    }

    private void configureServer(Service http) {
        http.port(4567);

        http.staticFiles.location("/public");
        http.staticFiles.expireTime(600);
    }

    private void configureRoutes(Service http) {
        indexController.configureRoutes(http);
        loginController.configureRoutes(http);
        userRegistrationController.configuresRoutes(http);
        userAdministrationController.configureRoutes(http);

        http.path("/api", () -> {
            http.before("/*", Filters::userIsLoggedIn);
            // TODO: Add api routes
        });
    }
}
