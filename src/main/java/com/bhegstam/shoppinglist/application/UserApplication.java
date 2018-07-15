package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.domain.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserApplication {
    private final UserRepository userRepository;

    public UserApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId addUser(String username, String password, String email) {
        User user = new User(username, password, email);
        userRepository.add(user);
        return user.getId();
    }

    public User getUser(UserId userId) {
        return userRepository.get(userId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(UserId userId, Role role, Boolean verified) {
        User user = userRepository.get(userId);
        Optional.ofNullable(role).ifPresent(user::setRole);
        Optional.ofNullable(verified).ifPresent(user::setVerified);

        userRepository.update(user);

        return user;
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }
}
