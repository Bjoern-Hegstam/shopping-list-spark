package com.bhegstam.shoppinglist.configuration.auth;

import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.Authenticator;
import org.jose4j.jwt.consumer.JwtContext;

import java.util.Optional;

public class JwtTokenAuthenticator implements Authenticator<JwtContext, User> {
    @Override
    public Optional<User> authenticate(JwtContext context) {
        // TODO: Build User from data in token
        return Optional.empty();
    }
}
