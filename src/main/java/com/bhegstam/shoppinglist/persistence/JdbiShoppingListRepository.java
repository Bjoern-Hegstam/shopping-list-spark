package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.bhegstam.shoppinglist.domain.ShoppingListId;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;

import java.util.List;

public interface JdbiShoppingListRepository extends ShoppingListRepository {
    @Override
    ShoppingList createShoppingList(String name);

    @Override
    ShoppingList get(ShoppingListId id);

    @Override
    List<ShoppingList> getShoppingLists();

    @Override
    void update(ShoppingList shoppingList);
}
