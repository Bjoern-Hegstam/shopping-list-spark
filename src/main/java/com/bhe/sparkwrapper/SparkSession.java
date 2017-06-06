package com.bhe.sparkwrapper;

import com.bhe.user.User;
import spark.Session;

public class SparkSession implements com.bhe.util.webapp.Session {
    private Session session;

    SparkSession(Session session) {
        this.session = session;
    }

    @Override
    public void setCurrentUser(User user) {
        session.attribute("currentUser", user);
    }

    @Override
    public boolean isUserLoggedIn() {
        return session.attribute("currentUser") != null;
    }

    @Override
    public void unsetCurrentUser() {
        session.removeAttribute("currentUser");
    }
}
