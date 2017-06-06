package com.bhe;

import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;

import static com.bhe.util.webapp.ResultBuilder.result;

public class IndexController {

    public Result serveIndexPage(Request request) {
        if (!request.session().isUserLoggedIn()) {
            return result().redirectTo(Path.Web.LOGIN);
        }

        return result().render(Path.Template.INDEX);
    }
}
