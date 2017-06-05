package com.bhe.login;

import spark.Request;
import spark.Response;
import spark.Route;

import static spark.Spark.halt;

public class LoginController {
    public static Route serveLoginPage = (request, responze) -> {
        // TODO: Render login page
        return null;
    };

    public static Route handleLoginPost = (request, response) -> {
        // TODO: Implement
        return null;
    };

    public static Route handleLogoutPost = (request, response) -> {
        // TODO: Implement
        return null;
    };

    public static boolean userIsLoggedIn(Request request) {
        return false;
    }

    public static void redirectIfUserNotAuthorized(Request request, Response response) {
        // TODO: Implement, redirect to login if not logged in
    }

    public static void userIsLoggedIn(Request request, Response response) {
        if (!userIsLoggedIn(request)) {
            halt(401, "Not authorized");
        }
    }
}
