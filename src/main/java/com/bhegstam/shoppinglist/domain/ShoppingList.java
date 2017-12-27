package com.bhegstam.shoppinglist.domain;

public class ShoppingList {
    private final String name;
    private final ShoppingListId id;

    public ShoppingList(ShoppingListId id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ShoppingListId getId() {
        return id;
    }
}
