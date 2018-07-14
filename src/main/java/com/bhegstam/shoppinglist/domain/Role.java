package com.bhegstam.shoppinglist.domain;

public enum Role {
    USER,
    ADMIN;

    public static class RoleName {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }

    public static Role fromString(String role) {
        if ("USER".equals(role)) {
            return USER;
        }

        if ("ADMIN".equals(role)) {
            return ADMIN;
        }

        return null;
    }
}
