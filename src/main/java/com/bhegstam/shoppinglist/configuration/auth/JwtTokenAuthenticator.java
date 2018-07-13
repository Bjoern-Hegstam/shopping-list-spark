package com.bhegstam.shoppinglist.configuration.auth;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;

import java.util.Optional;

public class JwtTokenAuthenticator implements Authenticator<JwtContext, User> {
    private final UserApplication userApplication;

    public JwtTokenAuthenticator(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @Override
    public Optional<User> authenticate(JwtContext context) throws AuthenticationException {
        JwtClaims claims = context.getJwtClaims();

        String username;
        try {
            username = claims.getSubject();
        } catch (MalformedClaimException e) {
            throw new AuthenticationException(e);
        }

        User user = userApplication
                .findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Unrecognized user " + username));

        return Optional.of(user);
    }
}
