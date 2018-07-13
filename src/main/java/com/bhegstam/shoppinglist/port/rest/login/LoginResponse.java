package com.bhegstam.shoppinglist.port.rest.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty
    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
