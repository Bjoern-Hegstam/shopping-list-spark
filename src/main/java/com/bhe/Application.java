package com.bhe;

import com.bhe.login.LoginController;
import com.bhe.util.Filters;
import com.bhe.util.Path;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        // Configure spark
        port(4567);

        staticFiles.location("/public");
        staticFiles.expireTime(600);

        get(Path.Web.INDEX, IndexController.serveIndexPage);

        get(Path.Web.LOGIN, LoginController.serveLoginPage);
        post(Path.Web.LOGIN, LoginController.handleLoginPost);
        post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        
        path("/api", () -> {
            before("/*", Filters::userIsLoggedIn);
            // TODO: Add api routes
        });
    }
}
