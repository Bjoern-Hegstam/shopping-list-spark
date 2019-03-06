package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.domain.UserId;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

class UserCreatedResponse {
    @JsonProperty
    private final String id;

    @JsonCreator
    UserCreatedResponse(UserId userId) {
        id = userId.getId();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
