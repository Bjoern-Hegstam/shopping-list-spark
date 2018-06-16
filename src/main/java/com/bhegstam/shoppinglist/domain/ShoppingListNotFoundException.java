package com.bhegstam.shoppinglist.domain;

public class ShoppingListNotFoundException extends RuntimeException {
    private final ShoppingListId listId;

    public ShoppingListNotFoundException(ShoppingListId listId) {
        this.listId = listId;
    }

    public ShoppingListId getListId() {
        return listId;
    }
}
