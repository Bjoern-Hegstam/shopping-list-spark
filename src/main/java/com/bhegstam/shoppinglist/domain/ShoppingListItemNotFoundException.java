package com.bhegstam.shoppinglist.domain;

public class ShoppingListItemNotFoundException extends RuntimeException {
    public ShoppingListItemNotFoundException(ShoppingListItemId itemId) {
    }
}
