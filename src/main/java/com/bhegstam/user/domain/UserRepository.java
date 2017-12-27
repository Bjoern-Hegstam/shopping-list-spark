package com.bhegstam.user.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserId create(User user);

    User get(UserId userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> getUsers();

    void update(User user);
}
