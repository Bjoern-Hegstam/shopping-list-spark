package com.bhe;

import com.bhe.login.LoginController;
import com.bhe.sparkwrapper.SparkRequest;
import com.bhe.user.User;
import com.bhe.user.UserRepositoryInMem;
import com.bhe.util.Filters;
import com.bhe.util.Path;
import com.bhe.util.ViewUtil;
import com.bhe.util.webapp.Result;
import com.bhe.util.webapp.Route;

import static spark.Spark.*;

public class Application {


    private static final UserRepositoryInMem userRepository = new UserRepositoryInMem();

    public static void main(String[] args) {
        userRepository.create(new User("a", "a"));

        // Configure spark
        port(4567);

        staticFiles.location("/public");
        staticFiles.expireTime(600);

        get(Path.Web.INDEX, asSparkRoute(new IndexController()::serveIndexPage));

        get(Path.Web.LOGIN, asSparkRoute(new LoginController(userRepository)::serveLoginPage));
        post(Path.Web.LOGIN, asSparkRoute(new LoginController(userRepository)::handleLoginPost));
        post(Path.Web.LOGOUT, asSparkRoute(new LoginController(userRepository)::handleLogoutPost));
        
        path("/api", () -> {
            before("/*", Filters::userIsLoggedIn);
            // TODO: Add api routes
        });
    }

    private static spark.Route asSparkRoute(Route route) {
        return (request, response) -> {
            Result result = route.handle(new SparkRequest(request));

            // TODO: Convert nullchecks to methods in Result and add unit tests
            if (result.redirectPath != null) {
                response.redirect(result.redirectPath);
                return null;
            }

            if (result.responsePayload != null) {
                return result.responsePayload;
            }

            if (result.renderTemplatePath != null) {
                return ViewUtil.render(request, result.renderModel, result.renderTemplatePath);
            }

            return null;
        };
    }
}
