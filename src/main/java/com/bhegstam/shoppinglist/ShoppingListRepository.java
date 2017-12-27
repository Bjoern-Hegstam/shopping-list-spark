package com.bhegstam.shoppinglist;

public interface ShoppingListRepository {
    ShoppingList createShoppingList(String name);

    ShoppingList get(ShoppingListId id);
}
