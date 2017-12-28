package com.bhegstam.shoppinglist.domain;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeId;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class ShoppingList {
    private final String name;
    private final ShoppingListId id;
    private final Map<ItemTypeId, ShoppingListItem> items;
    private final Set<ShoppingListItemId> removedItems;

    public ShoppingList(ShoppingListId id, String name) {
        this.id = id;
        this.name = name;
        items = new HashMap<>();
        removedItems = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public ShoppingListId getId() {
        return id;
    }

    public ShoppingListItem add(ItemType itemType) {
        ShoppingListItem item = items.computeIfAbsent(itemType.getId(), k -> new ShoppingListItem(itemType));
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
        ShoppingListItem item = items.remove(itemTypeId);
        removedItems.add(item.getId());
    }

    public void removeItemsInCart() {
        List<ItemTypeId> itemTypeIds = items
                .entrySet().stream()
                .filter(e -> e.getValue().isInCart())
                .map(Map.Entry::getKey)
                .collect(toList());

        itemTypeIds.forEach(this::remove);
    }

    public Collection<ShoppingListItemId> removedItemIds() {
        return Collections.unmodifiableCollection(removedItems);
    }

    public Collection<ShoppingListItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public void setItems(Collection<ShoppingListItem> items) {
        this.items.clear();
        this.removedItems.clear();
        items.forEach(item -> this.items.put(item.getItemType().getId(), item));
    }
}
