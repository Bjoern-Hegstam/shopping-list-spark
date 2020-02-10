package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.ItemTypeNotFoundException;
import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;

import java.time.Instant;
import java.util.*;

import static com.bhegstam.shoppinglist.util.CustomCollectors.onlyOptionalElement;
import static java.util.stream.Collectors.toList;

public class ShoppingList extends Entity<ShoppingListId> {
    private String name;
    private final Map<ItemTypeId, ItemType> itemTypes;
    private final Set<ItemTypeId> deletedItemTypes;
    private final Map<ItemTypeId, ShoppingListItem> items;
    private final Set<ShoppingListItemId> removedItems;

    public static ShoppingList create(String name) {
        return new ShoppingList(
                new ShoppingListId(),
                name,
                Instant.now(),
                null,
                PersistenceStatus.INSERT_REQUIRED
        );
    }

    public static ShoppingList fromDb(String id, String name, Instant createdAt, Instant updatedAt) {
        return new ShoppingList(
                ShoppingListId.parse(id),
                name,
                createdAt,
                updatedAt,
                PersistenceStatus.PERSISTED
        );
    }

    private ShoppingList(
            ShoppingListId id,
            String name,
            Instant createdAt,
            Instant updatedAt,
            PersistenceStatus persistenceStatus
    ) {
        super(id, createdAt, updatedAt, persistenceStatus);
        this.name = name;
        itemTypes = new HashMap<>();
        deletedItemTypes = new HashSet<>();
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

    public ItemType addItemType(String name) {
        Optional<ItemType> existingItemType = itemTypes
                .values().stream()
                .filter(itemType -> itemType.getName().equals(name))
                .collect(onlyOptionalElement());
        if (existingItemType.isPresent()) {
            throw new ItemTypeNameAlreadyTakenException(existingItemType.get());
        }

        ItemType itemType = ItemType.create(name);
        itemTypes.put(itemType.getId(), itemType);
        markAsUpdated();
        return itemType;
    }

    public List<ItemType> getItemTypes() {
        return new ArrayList<>(itemTypes.values());
    }

    public ItemType getItemType(ItemTypeId itemTypeId) {
        if (itemTypes.containsKey(itemTypeId)) {
            return itemTypes.get(itemTypeId);
        }

        throw new ItemTypeNotFoundException(itemTypeId);
    }

    public void deleteItemType(ItemTypeId itemTypeId) {
        if (items.containsKey(itemTypeId)) {
            throw new ItemTypeUsedInShoppingListException(itemTypeId);
        }

        itemTypes.remove(itemTypeId);
        deletedItemTypes.add(itemTypeId);
        markAsUpdated();
    }

    public Collection<ItemTypeId> deletedItemTypeIds() {
        return Collections.unmodifiableCollection(deletedItemTypes);
    }

    public void clearDeletedItemTypes() {
        deletedItemTypes.clear();
    }

    public ShoppingListItem add(ItemType itemType) {
        ShoppingListItem item = items.computeIfAbsent(itemType.getId(), k -> ShoppingListItem.create(itemType));
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

    public void clearRemovedItems() {
        removedItems.clear();
    }

    public Collection<ShoppingListItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public void loadFromDb(List<ItemType> itemTypes, Collection<ShoppingListItem> items) {
        this.itemTypes.clear();
        this.deletedItemTypes.clear();
        this.items.clear();
        this.removedItems.clear();
        itemTypes.forEach(itemType -> this.itemTypes.put(itemType.getId(), itemType));
        items.forEach(item -> this.items.put(item.getItemType().getId(), item));
    }
}
