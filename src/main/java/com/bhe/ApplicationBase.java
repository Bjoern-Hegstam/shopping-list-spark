package com.bhe;

import com.bhe.util.Filters;
import com.bhe.util.webapp.Controller;
import spark.Service;

import java.util.List;

import static spark.Service.ignite;

public abstract class ApplicationBase {
    private final List<Controller> appControllers;
    private final List<Controller> apiControllers;

    public ApplicationBase(List<Controller> appControllers, List<Controller> apiControllers) {
        this.appControllers = appControllers;
        this.apiControllers = apiControllers;
    }

    public void init() {
        Service http = ignite();
        configureServer(http);
        configureRoutes(http);
    }

    protected abstract void configureServer(Service http);

    private void configureRoutes(Service http) {
        appControllers.forEach(c -> c.configureRoutes(http));

        http.path("/api", () -> {
            http.before("/*", Filters::userIsLoggedIn);
            apiControllers.forEach(c -> c.configureRoutes(http));
        });
    }
}
