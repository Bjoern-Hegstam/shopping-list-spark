package com.bhegstam.shoppinglist;

import java.util.List;

public interface ShoppingListRepository {
    ShoppingList createShoppingList(String name);

    ShoppingList get(ShoppingListId id);

    List<ShoppingList> getShoppingLists();
}
