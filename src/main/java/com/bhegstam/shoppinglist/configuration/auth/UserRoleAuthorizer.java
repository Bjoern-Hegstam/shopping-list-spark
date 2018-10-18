package com.bhegstam.shoppinglist.configuration.auth;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.Authorizer;

public class UserRoleAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        // TODO: Unit test
        return user.getRole().equals(roleToDomain(role));
    }

    private Role roleToDomain(String role) {
        switch (role) {
            case "ADMIN":
                return Role.ADMIN;
            case "USER":
                return Role.USER;
            default:
                return null;
        }
    }
}