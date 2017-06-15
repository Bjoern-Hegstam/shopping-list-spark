package com.bhe.user;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class User {
    private final String username;
    private final String email;
    private final String hashedPassword;
    private final String salt;
    private final boolean verified;

    public User(String username, String password, String email) {
        this(username, password, email, false);
    }

    public User(String username, String password, String email, boolean verified) {
        this.username = Objects.requireNonNull(username);
        this.email = Objects.requireNonNull(email);
        this.verified = verified;
        this.salt = Objects.requireNonNull(BCrypt.gensalt());
        this.hashedPassword = BCrypt.hashpw(password, this.salt);
    }

    public boolean hasPassword(String password) {
        return BCrypt.hashpw(password, salt).equals(hashedPassword);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean hasValidUsername() {
        return !username.isEmpty();
    }

    public boolean hasValidEmail() {
        return EmailValidator
                .getInstance()
                .isValid(this.email);
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isAdmin() {
        return true;
    }
}
