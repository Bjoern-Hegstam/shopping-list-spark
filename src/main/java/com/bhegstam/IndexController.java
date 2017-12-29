package com.bhegstam;

import com.bhegstam.util.Path;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import spark.Service;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;


class IndexController implements Controller {

    @Override
    public void configureRoutes(Service http) {
        http.get(Path.Web.INDEX, asSparkRoute(this::serveIndexPage));
    }

    private Result serveIndexPage(Request request) {
        if (!request.session().isUserLoggedIn()) {
            return result().redirectTo(Path.Web.LOGIN);
        }

        return result().redirectTo(Path.Web.SHOPPING_LIST);
    }
}
