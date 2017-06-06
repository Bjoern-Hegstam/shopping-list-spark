package com.bhe.util;

import com.bhe.sparkwrapper.SparkRequest;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class Filters {
    public static void userIsLoggedIn(Request request, Response response) {
        boolean userIsLoggedIn = new SparkRequest(request).session().isUserLoggedIn();
        if (!userIsLoggedIn) {
            halt(401, "Not authorized");
        }
    }
}
