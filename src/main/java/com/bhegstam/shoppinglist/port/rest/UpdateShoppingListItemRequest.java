package com.bhegstam.shoppinglist.port.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Getter
public class UpdateShoppingListItemRequest {
    private final int quantity;
    private final boolean inCart;

    @JsonCreator
    public UpdateShoppingListItemRequest(
            @JsonProperty("quantity") int quantity,
            @JsonProperty("inCart") boolean inCart
    ) {
        this.quantity = quantity;
        this.inCart = inCart;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
