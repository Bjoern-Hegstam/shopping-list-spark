package com.bhegstam;

import com.bhegstam.util.Path;
import com.bhegstam.webutil.webapp.Controller;
import spark.Route;
import spark.Service;

import java.util.stream.Stream;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;


public class IndexController implements Controller {

    @Override
    public void configureRoutes(Service http) {
        Route indexRoute = asSparkRoute(request -> result().render(Path.Template.INDEX));

        Stream.of(
                "/",
                "/login",
                "/register",
                "/lists",
                "/lists/*",
                "/admin/*"
        ).forEach(path -> http.get(path, indexRoute));
    }
}
