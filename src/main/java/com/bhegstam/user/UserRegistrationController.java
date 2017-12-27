package com.bhegstam.user;

import com.bhegstam.util.Message;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class UserRegistrationController implements Controller {
    private final UserRegistration userRegistration;

    @Inject
    public UserRegistrationController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    @Override
    public void configureRoutes(Service http) {
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