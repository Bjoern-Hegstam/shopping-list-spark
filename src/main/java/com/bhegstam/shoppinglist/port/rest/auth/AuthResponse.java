package com.bhegstam.shoppinglist.port.rest.auth;

import com.bhegstam.shoppinglist.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;

class AuthResponse {
    @JsonProperty
    private final String token;

    @JsonProperty
    private final UserDto user;

    AuthResponse(String token, User user) {
        this.token = token;
        this.user = new UserDto(user);
    }
}
