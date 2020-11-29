package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;

public class AddShoppingListItemToCartRequest {
    @NotEmpty
    private final String shoppingListItemId;

    @JsonCreator
    AddShoppingListItemToCartRequest(
            @JsonProperty("shoppingListItemId") String shoppingListItemId
    ) {
        this.shoppingListItemId = shoppingListItemId;
    }

    public String getShoppingListItemId() {
        return shoppingListItemId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
