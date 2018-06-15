package com.bhegstam.shoppinglist.domain;

import java.util.UUID;

public class ShoppingListId extends Identifier {

    public ShoppingListId() {
        super();
    }

    public ShoppingListId(UUID id) {
        super(id);
    }

    public static ShoppingListId fromString(String s) {
        return new ShoppingListId(UUID.fromString(s));
    }
}
