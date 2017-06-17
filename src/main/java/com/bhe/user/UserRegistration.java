package com.bhe.user;

import com.google.inject.Inject;

import java.util.Optional;

public class UserRegistration {

    private final UserRepository userRepository;

    @Inject
    public UserRegistration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    boolean register(User user) {
        return Optional
                .of(user)
                .filter(User::hasValidUsername)
                .filter(User::hasValidEmail)
                .filter(this::usernameNotInUse)
                .filter(this::emailNotInUse)
                .map(userRepository::create)
                .orElse(false);
    }

    private boolean usernameNotInUse(User user) {
        return !userRepository
                .findByUsername(user.getUsername())
                .isPresent();
    }

    private boolean emailNotInUse(User user) {
        return !userRepository
                .findByEmail(user.getEmail())
                .isPresent();
    }
}
