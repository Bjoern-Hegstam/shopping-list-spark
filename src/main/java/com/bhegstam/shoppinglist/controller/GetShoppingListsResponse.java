package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingList;

import java.util.List;

import static java.util.stream.Collectors.toList;

class GetShoppingListsResponse {
    private final List<ShoppingListBean> shoppingLists;

    GetShoppingListsResponse(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists
                .stream()
                .map(ShoppingListBean::fromShoppingList)
                .collect(toList());
    }

    public List<ShoppingListBean> getShoppingLists() {
        return shoppingLists;
    }
}
