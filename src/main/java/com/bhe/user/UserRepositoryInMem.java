package com.bhe.user;

import java.util.*;

public class UserRepositoryInMem implements UserRepository {
    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();

    @Override
    public boolean create(User user) {
        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);
        return true;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.getOrDefault(username, null));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.getOrDefault(email, null));
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(usersByUsername.values());
    }
}
