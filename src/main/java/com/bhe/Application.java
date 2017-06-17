package com.bhe;

import com.bhe.admin.UserAdministrationController;
import com.bhe.login.LoginController;
import com.bhe.user.*;
import com.bhe.util.Filters;
import spark.Service;

import static spark.Service.ignite;

public class Application {
    private final UserRepository userRepository;
    private final IndexController indexController;
    private final LoginController loginController;
    private final UserRegistrationController userRegistrationController;
    private final UserAdministrationController userAdministrationController;

    public static void main(String[] args) {
        new Application().init();
    }
    
    private Application() {
        userRepository = new UserRepositoryInMem();

        indexController = new IndexController();
        loginController = new LoginController(userRepository);
        userRegistrationController = new UserRegistrationController(new UserRegistration(userRepository));
        userAdministrationController = new UserAdministrationController(userRepository);
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
