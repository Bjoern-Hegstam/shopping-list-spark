package com.bhe.util.webapp;

import com.bhe.user.User;

import java.util.Optional;

public interface Session {
    void setCurrentUser(User user);

    Optional<User> currentUser();

    boolean isUserLoggedIn();

    void unsetCurrentUser();

    void setErrorMessage(String msg);

    Optional<String> errorMessage();

    void clearErrorMessage();

    String locale();
}
