package com.bhegstam.shoppinglist.domain;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.security.Principal;
import java.util.Objects;

public class User implements Principal {
    private final UserId id;
    private final String username;
    private final String email;
    private final String hashedPassword;
    private final String salt;
    private boolean verified;
    private Role role;

    public User(String username, String password, String email) {
        this(username, password, email, false, Role.USER);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String getName() {
        return username;
    }
}
