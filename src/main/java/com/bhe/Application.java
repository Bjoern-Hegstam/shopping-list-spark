package com.bhe;

import com.bhe.login.LoginController;
import com.bhe.util.Filters;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        // Configure spark
        port(4567);

        staticFiles.location("/public");
        staticFiles.expireTime(600);

        get("/", IndexController.serveIndexPage);

        get("/login", LoginController.serveLoginPage);
        post("/login", LoginController.handleLoginPost);
        post("/logout", LoginController.handleLogoutPost);
        
        path("/api", () -> {
            before("/*", Filters::userIsLoggedIn);
            // TODO: Add api routes
        });
    }
}
