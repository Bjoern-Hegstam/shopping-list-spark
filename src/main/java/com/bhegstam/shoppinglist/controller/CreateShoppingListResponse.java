package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingListId;

public class CreateShoppingListResponse {
    private String id;

    public CreateShoppingListResponse(ShoppingListId id) {
        this.id = id.toString();
    }

    public String getId() {
        return id;
    }
}
