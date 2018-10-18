package com.bhegstam.shoppinglist.domain;

import java.util.Optional;

public class UserRegistration {

    private final UserRepository userRepository;

    public UserRegistration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(User user) {
        boolean userReqistrationAllowed = Optional
                .of(user)
                .filter(User::hasValidUsername)
                .filter(User::hasValidEmail)
                .filter(this::usernameNotInUse)
                .filter(this::emailNotInUse)
                .isPresent();

        if (userReqistrationAllowed) {
            userRepository.add(user);
        }

        return userReqistrationAllowed;
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
