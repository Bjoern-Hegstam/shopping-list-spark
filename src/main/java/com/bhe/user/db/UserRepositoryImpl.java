package com.bhe.user.db;

import com.bhe.db.DatabaseConnectionFactory;
import com.bhe.user.User;
import com.bhe.user.UserRepository;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final DatabaseConnectionFactory connectionFactory;

    @Inject
    public UserRepositoryImpl(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public boolean create(User user) {
        // TODO: Implement
        return false;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> getUsers() {
        return Collections.emptyList();
    }
}
