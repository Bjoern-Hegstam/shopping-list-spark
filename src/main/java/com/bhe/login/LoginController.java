package com.bhe.login;

import com.bhe.user.User;
import com.bhe.util.Path;
import com.bhe.util.Repositories;
import com.bhe.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

        Optional<User> user = Repositories
                .users()
                .findByUsername(username)
                .filter(u -> u.passwordIsValid(password));

        if (!user.isPresent()) {
            model.put("errorMessage", "LOGIN_AUTH_FAILED");
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        }

        request.session().attribute("currentUser", user.get());
        response.redirect(Path.Web.INDEX);
        return null;
    };

    public static Route handleLogoutPost = (request, response) -> {
        request.session().removeAttribute("currentUser");
        response.redirect(Path.Web.LOGIN);
        return null;
    };

    public static boolean userIsLoggedIn(Request request) {
        return request.session().attribute("currentUser") != null;
    }
}
