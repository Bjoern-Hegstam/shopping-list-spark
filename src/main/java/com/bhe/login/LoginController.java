package com.bhe.login;

import com.bhe.user.User;
import com.bhe.user.UserRepository;
import com.bhe.util.Message;
import com.bhe.util.Path;
import com.bhe.util.webapp.Controller;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.bhe.util.webapp.ResultBuilder.result;
import static com.bhe.util.webapp.SparkWrappers.asSparkRoute;

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
