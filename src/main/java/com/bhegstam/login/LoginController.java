package com.bhegstam.login;

import com.bhegstam.user.domain.User;
import com.bhegstam.user.domain.UserRepository;
import com.bhegstam.util.Message;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;


public class LoginController implements Controller {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

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
            logger.debug("User logged in, redirecting to index page");
            return result().redirectTo(Path.Web.INDEX);
        }

        logger.debug("Serving login page");
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
        logger.debug("User {} has logged in. Redirecting to {}", user.get().getId(), Path.Web.INDEX);

        return result().redirectTo(Path.Web.INDEX);
    }

    Result handleLogoutPost(Request request) {
        logger.debug("Logging out");
        request.session().unsetCurrentUser();
        return result().redirectTo(Path.Web.LOGIN);
    }
}
