package com.bhegstam.shoppinglist.domain;

import java.util.List;

public interface ShoppingListRepository {
    void persist(ShoppingList shoppingList);

    ShoppingList get(ShoppingListId id);

    List<ShoppingList> getShoppingLists();

    void delete(ShoppingListId listId);
}
