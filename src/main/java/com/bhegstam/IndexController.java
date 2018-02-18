package com.bhegstam;

import com.bhegstam.util.Path;
import com.bhegstam.webutil.webapp.Controller;
import spark.Service;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;


class IndexController implements Controller {

    @Override
    public void configureRoutes(Service http) {
        http.get("/*", asSparkRoute(request -> result().render(Path.Template.INDEX)));
    }
}
