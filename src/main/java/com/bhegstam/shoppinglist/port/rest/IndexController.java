package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.webutil.webapp.Controller;
import spark.Service;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;


public class IndexController implements Controller {

    @Override
    public void configureRoutes(Service http) {
        http.get("/", asSparkRoute(request -> result().render(Path.Template.INDEX)));
    }
}
