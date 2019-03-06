package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

class ShoppingListDto {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final String name;

    ShoppingListDto(ShoppingList shoppingList) {
        this.id = shoppingList.getId().getId();
        this.name = shoppingList.getName();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
