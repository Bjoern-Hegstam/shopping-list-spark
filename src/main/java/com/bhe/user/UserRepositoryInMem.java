package com.bhe.user;

import java.util.*;

public class UserRepositoryInMem implements UserRepository {
    private Map<String, User> users = new HashMap<>();

    @Override
    public void create(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.getOrDefault(username, null));
    }
}
