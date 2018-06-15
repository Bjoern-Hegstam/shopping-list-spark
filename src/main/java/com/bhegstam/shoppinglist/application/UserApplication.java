package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.shoppinglist.port.rest.UserBean;

import java.util.Optional;

public class UserApplication {
    private final UserRepository userRepository;

    public UserApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId addUser(String username, String password, String email) {
        return userRepository.create(new User(username, password, email));
    }

    public User getUser(UserId userId) {
        return userRepository.get(userId);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(UserId userId, UserBean userBean) {
        User user = userRepository.get(userId);
        Optional.ofNullable(userBean.getRole()).ifPresent(user::setRole);
        Optional.ofNullable(userBean.getVerified()).ifPresent(user::setVerified);

        userRepository.update(user);

        return user;
    }
}
