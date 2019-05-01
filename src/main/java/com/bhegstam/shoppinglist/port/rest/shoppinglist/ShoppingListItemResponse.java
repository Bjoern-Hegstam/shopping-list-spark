package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.domain.ShoppingListItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ShoppingListItemResponse {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final ItemTypeDto itemType;

    @JsonProperty
    private final Integer quantity;

    @JsonProperty
    private final Boolean inCart;

    ShoppingListItemResponse(ShoppingListItem item) {
        this.id = item.getId().getId();
        this.itemType = new ItemTypeDto(item.getItemType());
        this.quantity = item.getQuantity();
        this.inCart = item.isInCart();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
