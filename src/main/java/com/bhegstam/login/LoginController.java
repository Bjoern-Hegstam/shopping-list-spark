package com.bhegstam.login;

import com.bhegstam.user.User;
import com.bhegstam.user.UserRepository;
import com.bhegstam.util.Message;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;


public class LoginController implements Controller {
    private final UserRepository userRepository;

    @Inject
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.get(Path.Web.LOGIN, asSparkRoute(this::serveLoginPage));
        http.post(Path.Web.LOGIN, asSparkRoute(this::handleLoginPost));
        http.post(Path.Web.LOGOUT, asSparkRoute(this::handleLogoutPost));
    }

    Result serveLoginPage(Request request) {
        if (request.session().isUserLoggedIn()) {
            return result().redirectTo(Path.Web.INDEX);
        }

        return result().render(Path.Template.LOGIN);
    }

    Result handleLoginPost(Request request) {
        Map<String, Object> model = new HashMap<>();

        String username = request.queryParams("username");
        String password = request.queryParams("password");

        Optional<User> user = userRepository
                .findByUsername(username)
                .filter(u -> u.hasPassword(password));

        if (!user.isPresent()) {
            request.session().setErrorMessage(Message.LOGIN_AUTH_FAILED);
            return result().render(Path.Template.LOGIN, model);
        }

        if (!user.get().isVerified()) {
            request.session().setErrorMessage(Message.LOGIN_USER_PENDING_VERIFICATION);
            return result().render(Path.Template.LOGIN, model);
        }

        request.session().setCurrentUser(user.get());
        return result().redirectTo(Path.Web.INDEX);
    }

    Result handleLogoutPost(Request request) {
        request.session().unsetCurrentUser();
        return result().redirectTo(Path.Web.LOGIN);
    }
}