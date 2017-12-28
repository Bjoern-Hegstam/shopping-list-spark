package com.bhegstam.shoppinglist.domain;

import com.bhegstam.itemtype.domain.ItemTypeId;

import java.util.HashMap;
import java.util.Map;

public class ShoppingList {
    private final String name;
    private final ShoppingListId id;
    private final Map<ItemTypeId, ShoppingListItem> items;

    public ShoppingList(ShoppingListId id, String name) {
        this.id = id;
        this.name = name;
        items = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public ShoppingListId getId() {
        return id;
    }

    public ShoppingListItem add(ItemTypeId itemTypeId) {
        ShoppingListItem item = items.computeIfAbsent(itemTypeId, k -> new ShoppingListItem());
        item.setQuantity(item.getQuantity() + 1);
        return item;
    }

    public boolean contains(ItemTypeId itemTypeId) {
        return items.containsKey(itemTypeId);
    }

    public ShoppingListItem get(ItemTypeId id) {
        return items.get(id);
    }

    public void remove(ItemTypeId itemTypeId) {
        items.remove(itemTypeId);
    }
}
