package com.bhe;

import com.bhe.login.LoginController;
import com.bhe.sparkwrapper.SparkRequest;
import com.bhe.user.User;
import com.bhe.user.UserRegistrationController;
import com.bhe.user.UserRegistration;
import com.bhe.user.UserRepositoryInMem;
import com.bhe.util.Filters;
import com.bhe.util.Path;
import com.bhe.util.ViewUtil;
import com.bhe.util.webapp.Result;
import com.bhe.util.webapp.Route;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class Application {


    private static final UserRepositoryInMem userRepository = new UserRepositoryInMem();

    public static void main(String[] args) {
        userRepository.create(new User("a", "a", "a@domain.com", true));
        userRepository.create(new User("b", "b", "b@domain.com", false));

        // Configure spark
        port(4567);

        staticFiles.location("/public");
        staticFiles.expireTime(600);

        get(Path.Web.INDEX, asSparkRoute(new IndexController()::serveIndexPage));

        get(Path.Web.LOGIN, asSparkRoute(new LoginController(userRepository)::serveLoginPage));
        post(Path.Web.LOGIN, asSparkRoute(new LoginController(userRepository)::handleLoginPost));
        post(Path.Web.LOGOUT, asSparkRoute(new LoginController(userRepository)::handleLogoutPost));

        get(Path.Web.REGISTER, asSparkRoute(new UserRegistrationController(new UserRegistration(userRepository))::serveRegistrationPage));
        post(Path.Web.REGISTER, asSparkRoute(new UserRegistrationController(new UserRegistration(userRepository))::registerNewUser));

        path("/api", () -> {
            before("/*", Filters::userIsLoggedIn);
            // TODO: Add api routes
        });
    }

    private static spark.Route asSparkRoute(Route route) {
        return (request, response) -> {
            Result result = route.handle(new SparkRequest(request));

            return parseResult(request, response, result);
        };
    }

    private static Object parseResult(Request request, Response response, Result result) {
        if (result.isRedirect()) {
            response.redirect(result.redirectPath);
            return null;
        }

        if (result.isPayloadResponse()) {
            return result.responsePayload;
        }

        if (result.isRender()) {
            return ViewUtil.render(new SparkRequest(request), result.renderModel, result.renderTemplatePath);
        }

        return null;
    }
}
