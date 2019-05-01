package com.bhegstam.shoppinglist.port.rest.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
class CreateUserRequest {
    @NotEmpty
    private final String username;

    @NotEmpty
    private final String password;

    @NotEmpty
    private final String email;

    public CreateUserRequest(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
