package com.bhe.user;

import com.bhe.util.Message;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import static com.bhe.util.webapp.ResultBuilder.result;
import static com.bhe.util.webapp.SparkWrappers.asSparkRoute;

public class UserRegistrationController {
    private final UserRegistration userRegistration;

    @Inject
    public UserRegistrationController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    public void configuresRoutes(Service http) {
        http.get(Path.Web.REGISTER, asSparkRoute(this::serveRegistrationPage));
        http.post(Path.Web.REGISTER, asSparkRoute(this::registerNewUser));
    }

    Result serveRegistrationPage(Request request) {
        return result().render(Path.Template.REGISTER);
    }

    Result registerNewUser(Request request) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        String email = request.queryParams("email");

        boolean registrationSucceeded = userRegistration.register(new User(username, password, email));
        if (registrationSucceeded) {
            return result().redirectTo(Path.Web.LOGIN);
        } else {
            request.session().setErrorMessage(Message.USER_REGISTRATION_FAILED);
            return result().render(Path.Template.REGISTER);
        }
    }
}
