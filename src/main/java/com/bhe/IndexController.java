package com.bhe;

import com.bhe.login.LoginController;
import com.bhe.util.Path;
import com.bhe.util.ViewUtil;
import spark.Route;

import java.util.HashMap;

public class IndexController {

    public static Route serveIndexPage = (request, response) -> {
        LoginController.redirectIfUserNotLoggedIn(request, response);

        return ViewUtil.render(request, new HashMap<>(), Path.Template.INDEX);
    };
}
