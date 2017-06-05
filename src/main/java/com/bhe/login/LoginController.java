package com.bhe.login;

import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.halt;

public class LoginController {
    public static Route serveLoginPage = (request, responze) -> {
        if (userIsLoggedIn(request)) {
            responze.redirect("/");
            return null;
        }

        // TODO: Render login page
        return "Login page";
    };

    public static Route handleLoginPost = (request, response) -> {
        // TODO: Implement
        return null;
    };

    public static Route handleLogoutPost = (request, response) -> {
        // TODO: Implement
        return null;
    };

    public static void redirectIfUserNotAuthorized(Request request, Response response) {
        if (!userIsLoggedIn(request)) {
            response.redirect("/login");
        }
    }

    public static boolean userIsLoggedIn(Request request) {
        return false;
    }
}
