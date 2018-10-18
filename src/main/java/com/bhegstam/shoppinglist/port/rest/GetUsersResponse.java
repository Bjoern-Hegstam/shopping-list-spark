package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GetUsersResponse {
    @JsonProperty
    private final List<UserResponse> users;

    public GetUsersResponse(List<User> users) {
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
