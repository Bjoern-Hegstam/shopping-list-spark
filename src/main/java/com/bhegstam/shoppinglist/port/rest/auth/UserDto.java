package com.bhegstam.shoppinglist.port.rest.auth;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;

class UserDto {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final String username;

    @JsonProperty
    private final String role;

    UserDto(User user) {
        id = Integer.toString(user.getId().getId());
        username = user.getUsername();
        role = user.isAdmin() ? Role.RoleName.ADMIN : Role.RoleName.USER;
    }
}
