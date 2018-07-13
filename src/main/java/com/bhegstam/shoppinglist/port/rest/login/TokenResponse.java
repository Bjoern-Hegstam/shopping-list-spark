package com.bhegstam.shoppinglist.port.rest.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    @JsonProperty
    private final String token;

    public TokenResponse(String token) {
        this.token = token;
    }
}
