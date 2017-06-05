package com.bhe;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        // Configure spark
        port(4567);

        staticFiles.location("/public");
        staticFiles.expireTime(600);

        get("/hello", (req, res) -> "Hello World!");
        // TODO: Organize routes better
        // TODO: Get basic administration site up and running with in memory storage
    }
}
