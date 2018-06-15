package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
class GetShoppingListResponse {
    private final String id;
    private final String name;
    private final List<ShoppingListItemBean> items;

    GetShoppingListResponse(ShoppingList shoppingList) {
        this.id = shoppingList.getId().getId().toString();
        this.name = shoppingList.getName();
        this.items = shoppingList
                .getItems().stream()
                .map(ShoppingListItemBean::new)
                .collect(toList());
    }
}
