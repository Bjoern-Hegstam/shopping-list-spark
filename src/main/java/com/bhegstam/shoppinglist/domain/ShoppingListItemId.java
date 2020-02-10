package com.bhegstam.shoppinglist.domain;

public class ShoppingListItemId extends Identifier {
    public ShoppingListItemId() {
        super();
    }

    public ShoppingListItemId(String id) {
        super(id);
    }

    public static ShoppingListItemId parse(String s) {
        return new ShoppingListItemId(s);
    }
}
