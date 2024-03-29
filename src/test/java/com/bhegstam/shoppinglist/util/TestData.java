package com.bhegstam.shoppinglist.util;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.Workspace;

import java.util.Optional;

public class TestData {
    public static final User ADMIN = new User("admin", "admin-1", "admin@admin.com", true, Role.ADMIN);
    public static final Workspace ADMIN_DEFAULT_WORKSPACE = Workspace.create("Workspace for admin", ADMIN);
    public static final String ADMIN_PASSWORD = "admin-1";

    public static final User USER = new User("user", "user-1", "user@user.com", true, Role.USER);
    public static final Workspace USER_DEFAULT_WORKSPACE = Workspace.create("Workspace for user", ADMIN);

    public static final User UNVERIFIED_USER = new User("unverified-user", "unverified-user-1", "unverified@user.com", false, Role.USER);
    public static final Workspace UNVERIFIED_USER_DEFAULT_WORKSPACE = Workspace.create("Workspace for unverified user", ADMIN);

    private static final String CONF_FILENAME = "CONF_FILENAME";
    private static final String DEFAULT_CONF_FILENAME = "test-config.yml";

    public static String getTestConfigFilename() {
        return Optional
                .ofNullable(System.getenv(CONF_FILENAME))
                .orElse(DEFAULT_CONF_FILENAME);
    }
}
