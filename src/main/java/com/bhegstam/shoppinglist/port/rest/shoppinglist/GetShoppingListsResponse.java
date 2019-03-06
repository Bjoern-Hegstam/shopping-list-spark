package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;

import static java.util.stream.Collectors.toList;

class GetShoppingListsResponse {
    @JsonProperty
    private final List<ShoppingListDto> shoppingLists;

    GetShoppingListsResponse(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists
                .stream()
                .map(ShoppingListDto::new)
                .collect(toList());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
