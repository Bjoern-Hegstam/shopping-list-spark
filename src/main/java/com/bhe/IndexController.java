package com.bhe;

import com.bhe.login.LoginController;
import spark.Route;

public class IndexController {

    public static Route serveIndexPage = (request, response) -> {
        LoginController.redirectIfUserNotAuthorized(request, response);

        // TODO: List shopping lists
        return null;
    };
}
