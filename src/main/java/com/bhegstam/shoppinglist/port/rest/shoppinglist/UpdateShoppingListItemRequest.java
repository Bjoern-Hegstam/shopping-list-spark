package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;

class UpdateShoppingListItemRequest {
    @Min(0)
    private final int quantity;
    private final boolean inCart;

    @JsonCreator
    UpdateShoppingListItemRequest(
            @JsonProperty("quantity") int quantity,
            @JsonProperty("inCart") boolean inCart
    ) {
        this.quantity = quantity;
        this.inCart = inCart;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isInCart() {
        return inCart;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
