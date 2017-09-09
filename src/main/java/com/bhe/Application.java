package com.bhe;

import com.bhe.configuration.ApplicationConfiguration;
import com.bhe.configuration.ConfigurationModule;
import com.bhe.login.LoginController;
import com.bhe.user.UserAdministrationController;
import com.bhe.user.UserModule;
import com.bhe.user.UserRegistrationController;
import com.bhe.user.api.UserApiController;
import com.bhe.util.Filters;
import com.bhe.util.webapp.Controller;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import spark.Service;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static spark.Service.ignite;

public class Application {

    private final ApplicationConfiguration configuration;
    private final List<Controller> appControllers;
    private final List<Controller> apiControllers;

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
            ApplicationConfiguration configuration,
            IndexController indexController,
            LoginController loginController,
            UserRegistrationController userRegistrationController,
            UserAdministrationController userAdministrationController,
            UserApiController userApiController
    ) {
        this.configuration = configuration;

        appControllers = asList(
                indexController,
                loginController,
                userRegistrationController,
                userAdministrationController
        );

        apiControllers = singletonList(
                userApiController
        );
    }

    private void init() {
        Service http = ignite();
        configureServer(http);
        configureRoutes(http);
    }

    private void configureServer(Service http) {
        http.port(configuration.getServer().getPort());

        http.staticFiles.location("/public");
        http.staticFiles.expireTime(600);
    }

    private void configureRoutes(Service http) {
        appControllers.forEach(c -> c.configureRoutes(http));

        http.path("/api", () -> {
            http.before("/*", Filters::userIsLoggedIn);
            apiControllers.forEach(c -> c.configureRoutes(http));
        });
    }
}
