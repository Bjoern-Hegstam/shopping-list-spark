package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.ShoppingListId;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Getter
class CreateShoppingListResponse {
    private final String id;

    CreateShoppingListResponse(ShoppingListId id) {
        this.id = id.getId();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
