package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
class GetShoppingListsResponse {
    private final List<ShoppingListInfo> shoppingLists;

    GetShoppingListsResponse(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists
                .stream()
                .map(ShoppingListInfo::new)
                .collect(toList());
    }
}
