package com.bhegstam.shoppinglist.domain;

import java.util.Objects;
import java.util.Optional;

public class UserRegistration {

    private final UserRepository userRepository;

    public UserRegistration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(User user) {
        return Optional
                .of(user)
                .filter(User::hasValidUsername)
                .filter(User::hasValidEmail)
                .filter(this::usernameNotInUse)
                .filter(this::emailNotInUse)
                .map(userRepository::create)
                .map(Objects::nonNull)
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
