package com.bhe.util.webapp;

public interface Request {

    Session session();

    String queryParams(String key);
}
