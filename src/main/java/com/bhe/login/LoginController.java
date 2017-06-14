package com.bhe.login;

import com.bhe.user.User;
import com.bhe.user.UserRepository;
import com.bhe.util.Message;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.bhe.util.webapp.ResultBuilder.result;

public class LoginController {
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result serveLoginPage(Request request) {
        if (request.session().isUserLoggedIn()) {
            return result().redirectTo(Path.Web.INDEX);
        }

        return result().render(Path.Template.LOGIN);
    }

    public Result handleLoginPost(Request request) {
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

    public Result handleLogoutPost(Request request) {
        request.session().unsetCurrentUser();
        return result().redirectTo(Path.Web.LOGIN);
    }
}
