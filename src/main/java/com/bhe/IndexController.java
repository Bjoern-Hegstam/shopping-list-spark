package com.bhe;

import com.bhe.util.Path;
import com.bhe.webutil.webapp.Controller;
import com.bhe.webutil.webapp.Request;
import com.bhe.webutil.webapp.Result;
import spark.Service;

import static com.bhe.webutil.webapp.ResultBuilder.result;
import static com.bhe.webutil.webapp.SparkWrappers.asSparkRoute;


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
