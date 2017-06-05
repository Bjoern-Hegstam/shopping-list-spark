package com.bhe.login;

import com.bhe.util.Path;
import com.bhe.util.Repositories;
import com.bhe.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class LoginController {
    public static Route serveLoginPage = (request, responze) -> {
        if (userIsLoggedIn(request)) {
            responze.redirect(Path.Web.INDEX);
            return null;
        }

        return ViewUtil.render(request, new HashMap<>(), Path.Template.LOGIN);
    };

    public static Route handleLoginPost = (request, response) -> {
        Map<String, Object> model = new HashMap<>();

        String username = request.queryParams("username");
        String password = request.queryParams("password");

        if (!areValidCredentials(username, password)) {
            model.put("errorMessage", "LOGIN_AUTH_FAILED");
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        }

        request.session().attribute("currentUser", username);
        response.redirect(Path.Web.INDEX);
        return null;
    };

    private static boolean areValidCredentials(String username, String password) {
        return Repositories
                .users()
                .findByUsername(username)
                .filter(user -> user.passwordIsValid(password))
                .isPresent();
    }

    public static Route handleLogoutPost = (request, response) -> {
        // TODO: Implement
        return null;
    };

    public static void redirectIfUserNotLoggedIn(Request request, Response response) {
        if (!userIsLoggedIn(request)) {
            response.redirect(Path.Web.LOGIN);
        }
    }

    public static boolean userIsLoggedIn(Request request) {
        return request.session().attribute("currentUser") != null;
    }
}
