package com.bhe.util.webapp;

import spark.Service;

public interface Controller {
    void configureRoutes(Service http);
}
