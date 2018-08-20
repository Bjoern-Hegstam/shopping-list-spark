package com.bhegstam.shoppinglist.port.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
public class UpdateShoppingListRequest {
    @NotEmpty
    private final String name;

    @JsonCreator
    public UpdateShoppingListRequest(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
