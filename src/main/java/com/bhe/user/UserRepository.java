package com.bhe.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Integer create(User user);

    User get(int userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> getUsers();

    void update(User user);
}
