package com.bhegstam.shoppinglist.configuration.auth;

import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

public class JwtTokenAuthenticator implements Authenticator<String, User> {
    @Override
    public Optional<User> authenticate(String credentials) throws AuthenticationException {
        // TODO: Build User from data in token
        return Optional.empty();
    }
}
