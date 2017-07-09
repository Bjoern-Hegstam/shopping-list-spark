package com.bhe.user;

import java.util.*;

public class UserRepositoryInMem implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();

    @Override
    public Integer create(User user) {
        User userWithId = new User(
                users.size(),
                user.getUsername(),
                user.getEmail(),
                user.getHashedPassword(),
                user.getSalt(),
                user.isVerified(),
                user.getRole()
        );

        addOrUpdateUser(userWithId);
        return userWithId.getId();
    }

    @Override
    public User get(int userId) {
        return users.get(userId);
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

    @Override
    public void update(User user) {
        addOrUpdateUser(user);
    }

    private void addOrUpdateUser(User user) {
        users.put(user.getId(), user);
        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);
    }
}
