package com.bhe.util;

import com.bhe.login.LoginController;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class Filters {
    public static void userIsLoggedIn(Request request, Response response) {
        boolean userIsLoggedIn = LoginController.userIsLoggedIn(request);
        if (!userIsLoggedIn) {
            halt(401, "Not authorized");
        }
    }
}
