package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.persistence.PersistenceStatus;

import java.util.*;

import static com.bhegstam.webutil.CustomCollectors.onlyElement;
import static java.util.stream.Collectors.toList;

public class ShoppingList extends Entity<ShoppingListId> {
    private final String name;
    private final Map<ItemTypeId, ShoppingListItem> items;
    private final Set<ShoppingListItemId> removedItems;
    private PersistenceStatus persistenceStatus;

    public ShoppingList(String name) {
        this(new ShoppingListId(), name, PersistenceStatus.INSERT_REQUIRED);
    }

    public ShoppingList(ShoppingListId id, String name, PersistenceStatus persistenceStatus) {
        super(id);
        this.name = name;
        items = new HashMap<>();
        removedItems = new HashSet<>();
        this.persistenceStatus = persistenceStatus;
    }

    public String getName() {
        return name;
    }

    public PersistenceStatus getPersistenceStatus() {
        return persistenceStatus;
    }

    public ShoppingListItem add(ItemType itemType) {
        ShoppingListItem item = items.computeIfAbsent(itemType.getId(), k -> new ShoppingListItem(itemType));
        item.setQuantity(item.getQuantity() + 1);
        return item;
    }

    public boolean contains(ItemTypeId itemTypeId) {
        return items.containsKey(itemTypeId);
    }

    public ShoppingListItem get(ItemTypeId itemTypeId) {
        return items.get(itemTypeId);
    }

    public ShoppingListItem get(ShoppingListItemId itemId) {
        return items
                .values().stream()
                .filter(item -> item.getId().equals(itemId))
                .collect(onlyElement());
    }

    public void remove(ItemTypeId itemTypeId) {
        ShoppingListItem item = items.remove(itemTypeId);
        removedItems.add(item.getId());
    }

    public void remove(ShoppingListItemId listItemId) {
        ItemTypeId itemTypeId = items
                .entrySet().stream()
                .filter(e -> e.getValue().getId().equals(listItemId))
                .map(Map.Entry::getKey)
                .collect(onlyElement());

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

    public void setPersistenceStatus(PersistenceStatus persistenceStatus) {
        this.persistenceStatus = persistenceStatus;
    }
}
