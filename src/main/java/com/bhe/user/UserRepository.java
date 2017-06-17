package com.bhe.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    boolean create(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> getUsers();
}
