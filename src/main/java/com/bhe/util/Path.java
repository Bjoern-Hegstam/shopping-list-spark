package com.bhe.util;

public class Path {

    public static class Web {
        public static final String INDEX = "/";
        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
        public static final String REGISTER = "/register";
        public static final String USERS = "/users";
        public static final String ADMIN = "/admin";
    }

    public static class Template {
        public static final String LOGIN = "/velocity/login/login.vm";
        public static final String INDEX = "/velocity/index/index.vm";
        public static final String REGISTER = "/velocity/user/register.vm";
        public static final String ADMIN_USERS = "velocity/admin/users.vm";
    }

    public static class Api {
        public static final String USER = "/user";
    }
}
