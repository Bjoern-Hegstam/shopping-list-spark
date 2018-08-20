package com.bhegstam.shoppinglist.domain;

import java.util.List;

public interface ShoppingListRepository {
    void add(ShoppingList shoppingList);

    ShoppingList get(ShoppingListId id);

    List<ShoppingList> getShoppingLists();

    void delete(ShoppingListId listId);
}
