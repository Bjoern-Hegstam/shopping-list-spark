package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import static java.util.stream.Collectors.toList;

class GetUsersResponse {
    @JsonProperty
    private final List<UserResponse> users;

    GetUsersResponse(List<User> users) {
        this.users = users
                .stream()
                .map(UserResponse::new)
                .collect(toList());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
