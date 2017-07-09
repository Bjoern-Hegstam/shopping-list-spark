package com.bhe.sparkwrapper;

import spark.Request;

public class SparkRequest implements com.bhe.util.webapp.Request {
    private final Request request;

    public SparkRequest(Request request) {
        this.request = request;
    }

    @Override
    public com.bhe.util.webapp.Session session() {
        return new SparkSession(request.session());
    }

    @Override
    public String params(String key) {
        return request.params(key);
    }

    @Override
    public String queryParams(String key) {
        return request.queryParams(key);
    }

    @Override
    public String body() {
        return request.body();
    }
}
