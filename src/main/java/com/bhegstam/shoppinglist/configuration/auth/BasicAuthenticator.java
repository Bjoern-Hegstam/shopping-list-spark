package com.bhegstam.shoppinglist.configuration.auth;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.basic.BasicCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BasicAuthenticator implements io.dropwizard.auth.Authenticator<BasicCredentials, User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticator.class);

    private final UserApplication userApplication;

    public BasicAuthenticator(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        Optional<User> user = userApplication
                .findByUsername(username)
                .filter(u -> u.hasPassword(password));

        if (!user.isPresent()) {
            LOGGER.error("Error while authenticating. User [{}] does not exist", credentials.getUsername());
            return Optional.empty();
        }

        if (!user.get().isVerified()) {
            LOGGER.warn("User [{}] pending verification", user.get().getId());
            return Optional.empty();
        }

        return user;
    }
}
