package com.bhegstam.shoppinglist.domain;

import java.util.List;

public interface ShoppingListRepository {
    void persist(UserId userId, ShoppingList shoppingList);

    ShoppingList get(UserId userId, ShoppingListId id);

    List<ShoppingList> getShoppingLists(UserId userId);

    void delete(UserId userId, ShoppingListId listId);
}
