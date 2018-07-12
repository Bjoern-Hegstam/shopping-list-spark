package com.bhegstam.shoppinglist.configuration.auth;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.port.rest.Message;
import io.dropwizard.auth.basic.BasicCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BasicAuthenticator implements io.dropwizard.auth.Authenticator<BasicCredentials, User> {
    private static final Logger LOGGER = LogManager.getLogger(BasicAuthenticator.class);

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
            LOGGER.error(Message.LOGIN_AUTH_FAILED);
            return Optional.empty();
        }

        if (!user.get().isVerified()) {
            LOGGER.error(Message.LOGIN_USER_PENDING_VERIFICATION);
            return Optional.empty();
        }

        return user;
    }
}
