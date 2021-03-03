package com.bhegstam.shoppinglist.port.rest.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

class UpdateUserRequest {
    private final boolean verified;

    @NotEmpty
    private final String role;

    UpdateUserRequest(
            @JsonProperty("verified") boolean verified,
            @JsonProperty("role") String role
    ) {
        this.verified = verified;
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
