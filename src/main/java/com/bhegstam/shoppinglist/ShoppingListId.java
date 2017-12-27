package com.bhegstam.shoppinglist;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public class ShoppingListId {

    private final UUID id;

    ShoppingListId() {
        id = UUID.randomUUID();
    }

    private ShoppingListId(UUID id) {
        this.id = id;
    }

    public static ShoppingListId fromString(String s) {
        return new ShoppingListId(UUID.fromString(s));
    }
}
