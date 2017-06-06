package com.bhe.util.webapp;

import com.bhe.user.User;

public interface Session {
    void setCurrentUser(User user);

    boolean isUserLoggedIn();

    void unsetCurrentUser();
}
