package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

class UserResponse {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final String username;

    @JsonProperty
    private final boolean verified;

    @JsonProperty
    private final String role;

    UserResponse(User user) {
        id = user.getId().getId();
        username = user.getUsername();
        verified = user.isVerified();
        role = translateUserRole(user.getRole());
    }

    private String translateUserRole(Role role) {
        switch (role) {
            case USER:
                return "USER";
            case ADMIN:
                return "ADMIN";
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
