package com.bhegstam.user;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class User implements com.bhegstam.webutil.webapp.User {
    private final UserId id;
    private final String username;
    private final String email;
    private final String hashedPassword;
    private final String salt;
    private boolean verified;
    private Role role;

    public User(String username, String password, String email) {
        this(username, password, email, false);
    }

    public User(String username, String password, String email, boolean verified) {
        this(username, password, email, verified, Role.USER);
    }

    public User(String username, String password, String email, boolean verified, Role role) {
        this.id = null;
        this.username = Objects.requireNonNull(username);
        this.email = Objects.requireNonNull(email);
        this.verified = verified;
        this.salt = Objects.requireNonNull(BCrypt.gensalt());
        this.hashedPassword = BCrypt.hashpw(password, this.salt);
        this.role = role;
    }

    public User(UserId id, String username, String email, String hashedPassword, String salt, boolean verified, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.verified = verified;
        this.role = role;
    }

    public boolean hasPassword(String password) {
        return BCrypt.hashpw(password, salt).equals(hashedPassword);
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean hasValidUsername() {
        return !username.isEmpty();
    }

    public boolean hasValidEmail() {
        return EmailValidator
                .getInstance()
                .isValid(this.email);
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}
