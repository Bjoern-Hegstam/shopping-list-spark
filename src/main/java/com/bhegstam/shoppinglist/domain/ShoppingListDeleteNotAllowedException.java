package com.bhegstam.shoppinglist.domain;

public class ShoppingListDeleteNotAllowedException extends RuntimeException {
    private final ShoppingListId listId;

    public ShoppingListDeleteNotAllowedException(ShoppingListId listId) {
        this.listId = listId;
    }
}
