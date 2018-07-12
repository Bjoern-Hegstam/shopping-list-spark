package com.bhegstam.shoppinglist.configuration.auth;

import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.Authorizer;

public class RoleAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}