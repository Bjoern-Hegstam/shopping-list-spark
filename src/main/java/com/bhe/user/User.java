package com.bhe.user;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    private String username;
    private String hashedPassword;
    private String salt;

    public User(String username, String password) {
        this.username = username;
        this.salt = BCrypt.gensalt();
        this.hashedPassword = BCrypt.hashpw(password, this.salt);
    }
    
    public boolean passwordIsValid(String password) {
        return BCrypt.hashpw(password, salt).equals(hashedPassword);
    }

    public String getUsername() {
        return username;
    }
}
