package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.bhegstam.webutil.CustomCollectors.onlyOptionalElement;
import static java.util.stream.Collectors.toList;

public class ShoppingList extends Entity<ShoppingListId> {
    private String name;
    private final Map<ItemTypeId, ShoppingListItem> items;
    private final Set<ShoppingListItemId> removedItems;

    public ShoppingList(String name) {
        this(new ShoppingListId(), name, PersistenceStatus.INSERT_REQUIRED);
    }

    public static ShoppingList fromDb(String id, String name) {
        return new ShoppingList(ShoppingListId.fromString(id), name, PersistenceStatus.PERSISTED);
    }

    private ShoppingList(ShoppingListId id, String name, PersistenceStatus persistenceStatus) {
        super(
                id,
                LocalDateTime.now(ZoneOffset.UTC),
                LocalDateTime.now(ZoneOffset.UTC),
                persistenceStatus
        );
        this.name = name;
        items = new HashMap<>();
        removedItems = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        markAsUpdated();
        this.name = name;
    }

    public ShoppingListItem add(ItemType itemType) {
        ShoppingListItem item = items.computeIfAbsent(itemType.getId(), k -> new ShoppingListItem(itemType));
        item.setQuantity(item.getQuantity() + 1);

        markAsUpdated();

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
                .collect(onlyOptionalElement())
                .orElseThrow(() -> new ShoppingListItemNotFoundException(itemId));
    }

    public void remove(ItemTypeId itemTypeId) {
        ShoppingListItem item = items.remove(itemTypeId);
        removedItems.add(item.getId());
        markAsUpdated();
    }

    public void remove(ShoppingListItemId listItemId) {
        ItemTypeId itemTypeId = items
                .entrySet().stream()
                .filter(e -> e.getValue().getId().equals(listItemId))
                .map(Map.Entry::getKey)
                .collect(onlyOptionalElement())
                .orElseThrow(() -> new ShoppingListItemNotFoundException(listItemId));

        ShoppingListItem item = items.remove(itemTypeId);
        removedItems.add(item.getId());
        markAsUpdated();
    }

    public void removeItemsInCart() {
        List<ItemTypeId> itemTypeIds = items
                .entrySet().stream()
                .filter(e -> e.getValue().isInCart())
                .map(Map.Entry::getKey)
                .collect(toList());

        itemTypeIds.forEach(this::remove);

        if (!itemTypeIds.isEmpty()) {
            markAsUpdated();
        }
    }

    public Collection<ShoppingListItemId> removedItemIds() {
        return Collections.unmodifiableCollection(removedItems);
    }

    public Collection<ShoppingListItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public void loadItemsFromDb(Collection<ShoppingListItem> items) {
        this.items.clear();
        this.removedItems.clear();
        items.forEach(item -> this.items.put(item.getItemType().getId(), item));
    }
}
