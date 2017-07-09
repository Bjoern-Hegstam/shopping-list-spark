package com.bhe.util.webapp;

public interface Request {

    Session session();

    String params(String key);

    String queryParams(String key);

    String body();
}
