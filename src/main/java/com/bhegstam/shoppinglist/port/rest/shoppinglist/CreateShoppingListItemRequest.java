package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
public class CreateShoppingListItemRequest {
    @NotEmpty
    private final String itemTypeId;

    @NotNull
    private final Integer quantity;

    @JsonCreator
    public CreateShoppingListItemRequest(
            @JsonProperty("itemTypeId") String itemTypeId,
            @JsonProperty("quantity") Integer quantity
    ) {
        this.itemTypeId = itemTypeId;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
