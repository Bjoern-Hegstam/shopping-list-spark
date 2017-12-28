package com.bhegstam.shoppinglist.domain;

import com.bhegstam.util.domain.Identifier;

import java.util.UUID;

public class ShoppingListItemId extends Identifier {
    public ShoppingListItemId() {
        super();
    }

    public ShoppingListItemId(UUID id) {
        super(id);
    }

    public static ShoppingListItemId fromString(String s) {
        return new ShoppingListItemId(UUID.fromString(s));
    }
}
