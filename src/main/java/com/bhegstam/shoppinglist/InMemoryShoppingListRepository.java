package com.bhegstam.shoppinglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryShoppingListRepository implements ShoppingListRepository {
    private final Map<ShoppingListId, ShoppingList> lists;

    public InMemoryShoppingListRepository() {
        lists = new HashMap<>();
    }

    @Override
    public ShoppingList createShoppingList(String name) {
        ShoppingList shoppingList = new ShoppingList(
                new ShoppingListId(),
                name
        );
        lists.put(shoppingList.getId(), shoppingList);

        return shoppingList;
    }

    @Override
    public ShoppingList get(ShoppingListId id) {
        return lists.get(id);
    }

    @Override
    public List<ShoppingList> getShoppingLists() {
        return new ArrayList<>(lists.values());
    }
}
