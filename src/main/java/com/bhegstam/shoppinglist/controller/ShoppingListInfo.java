package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ShoppingListInfo {
    private final String id;
    private final String name;

    public ShoppingListInfo(ShoppingList shoppingList) {
        this.id = shoppingList.getId().getId().toString();
        this.name = shoppingList.getName();
    }
}
