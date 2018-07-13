package com.bhegstam.shoppinglist.util;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;

public class TestData {
    public static final User ADMIN = new User("admin", "admin-1", "admin@admin.com", true, Role.ADMIN);
    public static final String ADMIN_PASSWORD = "admin-1";

    public static final User USER = new User("user", "user-1", "user@user.com", true, Role.USER);
    public static final User UNVERIFIED_USER = new User("unverified-user", "unverified-user-1", "unverified@user.com", false, Role.USER);

    static final String TEST_CONFIG_FILENAME = "test-config.yml";

}
