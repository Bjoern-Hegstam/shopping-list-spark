package com.bhegstam.shoppinglist.domain;

public class ShoppingListId extends Identifier {

    public ShoppingListId() {
        super();
    }

    public ShoppingListId(String id) {
        super(id);
    }

    public static ShoppingListId fromString(String s) {
        return new ShoppingListId(s);
    }
}
