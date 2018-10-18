package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
class GetShoppingListResponse {
    private final String id;
    private final String name;
    private final List<ShoppingListItemResponse> items;

    GetShoppingListResponse(ShoppingList shoppingList) {
        this.id = shoppingList.getId().getId();
        this.name = shoppingList.getName();
        this.items = shoppingList
                .getItems().stream()
                .sorted(Comparator.comparing(item -> item.getItemType().getName()))
                .map(ShoppingListItemResponse::new)
                .collect(toList());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
