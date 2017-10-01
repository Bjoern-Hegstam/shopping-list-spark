package com.bhe;

import com.bhe.configuration.ApplicationConfiguration;
import com.bhe.configuration.ConfigurationModule;
import com.bhe.login.LoginController;
import com.bhe.user.UserAdministrationController;
import com.bhe.user.UserModule;
import com.bhe.user.UserRegistrationController;
import com.bhe.user.api.UserApiController;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import spark.Service;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class Application extends ApplicationBase {

    private final ApplicationConfiguration configuration;

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
        super(
                asList(
                        indexController,
                        loginController,
                        userRegistrationController,
                        userAdministrationController
                ),
                singletonList(
                        userApiController
                )
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
