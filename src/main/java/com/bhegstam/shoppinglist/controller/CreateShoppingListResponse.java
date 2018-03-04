package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingListId;
import lombok.Getter;

@Getter
class CreateShoppingListResponse {
    private final String id;

    CreateShoppingListResponse(ShoppingListId id) {
        this.id = id.toString();
    }
}
