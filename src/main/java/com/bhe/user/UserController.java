package com.bhe.user;

import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;

import static com.bhe.util.webapp.ResultBuilder.result;

public class UserController {
    private final UserRegistration userRegistration;

    public UserController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    public Result serveRegistrationPage(Request request) {
        return result().render(Path.Template.REGISTER);
    }

    public Result registerNewUser(Request request) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String email = request.queryParams("email");

        boolean registrationSucceeded = userRegistration.registerNewUser(username, password, email);
        if (registrationSucceeded) {
            return result().redirectTo(Path.Web.LOGIN);
        } else {
            request.session().setErrorMessage("USER_REGISTER_NOT_ALLOWED");
            return result().render(Path.Template.REGISTER);
        }
    }
}
