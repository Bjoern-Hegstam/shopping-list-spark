package com.bhegstam.shoppinglist.port.rest.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
class UpdateUserRequest {
    private final Boolean verified;

    private final String role;

    UpdateUserRequest(
            @JsonProperty("verified") Boolean verified,
            @JsonProperty("role") String role
    ) {
        this.verified = verified;
        this.role = role;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
