package com.bhegstam.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDto {
    private String username;
    private String password;

    public LoginDto(
            @JsonProperty(value = "username", required = true) String username,
            @JsonProperty(value = "password", required = true) String password
    ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
