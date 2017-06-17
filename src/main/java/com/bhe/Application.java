package com.bhe;

import com.bhe.admin.UserAdministrationController;
import com.bhe.login.LoginController;
import com.bhe.user.*;
import com.bhe.util.Filters;
import com.google.inject.Guice;
import com.google.inject.Inject;
import spark.Service;

import static spark.Service.ignite;

public class Application {
    private final UserRepository userRepository;

    private final IndexController indexController;
    private final LoginController loginController;
    private final UserRegistrationController userRegistrationController;
    private final UserAdministrationController userAdministrationController;

    public static void main(String[] args) {
        Guice.createInjector(new UserModule())
             .getInstance(Application.class)
             .init();
    }

    @Inject
    public Application(
            UserRepository userRepository,
            IndexController indexController,
            LoginController loginController,
            UserRegistrationController userRegistrationController,
            UserAdministrationController userAdministrationController
    ) {
        this.userRepository = userRepository;
        this.indexController = indexController;
        this.loginController = loginController;
        this.userRegistrationController = userRegistrationController;
        this.userAdministrationController = userAdministrationController;
    }

    private void init() {
        createTestUsers();

        Service http = ignite();
        configureServer(http);
        configureRoutes(http);
    }

    private void createTestUsers() {
        userRepository.create(new User("a", "a", "a@domain.com", true, Role.ADMIN));
        userRepository.create(new User("b", "b", "b@domain.com", true));
        userRepository.create(new User("c", "c", "b@domain.com", false));
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
