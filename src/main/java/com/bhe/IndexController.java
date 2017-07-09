package com.bhe;

import com.bhe.util.Path;
import com.bhe.util.webapp.Controller;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import spark.Service;

import static com.bhe.util.webapp.ResultBuilder.result;
import static com.bhe.util.webapp.SparkWrappers.asSparkRoute;

class IndexController implements Controller {

    @Override
    public void configureRoutes(Service http) {
        http.get(Path.Web.INDEX, asSparkRoute(this::serveIndexPage));
    }

    private Result serveIndexPage(Request request) {
        if (!request.session().isUserLoggedIn()) {
            return result().redirectTo(Path.Web.LOGIN);
        }

        return result().render(Path.Template.INDEX);
    }
}
