package com.bhe.sparkwrapper;

import com.bhe.user.User;
import spark.Session;

import java.util.Optional;

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
    public Optional<User> currentUser() {
        return Optional.ofNullable(session.attribute("currentUser"));
    }

    @Override
    public boolean isUserLoggedIn() {
        return session.attribute("currentUser") != null;
    }

    @Override
    public void unsetCurrentUser() {
        session.removeAttribute("currentUser");
    }

    @Override
    public void setErrorMessage(String msg) {
        session.attribute("errorMessage", msg);
    }

    @Override
    public Optional<String> errorMessage() {
        return Optional.ofNullable(session.attribute("errorMessage"));
    }

    @Override
    public void clearErrorMessage() {
        session.removeAttribute("errorMessage");
    }

    @Override
    public String locale() {
        return session.attribute("locale");
    }
}
