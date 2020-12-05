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
    private final List<ShoppingListItem> items;
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
        items = new ArrayList<>();
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
        boolean itemTypeInUse = items
                .stream()
                .anyMatch(item -> item.getItemType().getId().equals(itemTypeId));

        if (itemTypeInUse) {
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

    public ShoppingListItem addItem(ItemType itemType) {
        return addItem(itemType, 1);
    }

    public ShoppingListItem addItem(ItemType itemType, int quantity) {
        ShoppingListItem item = ShoppingListItem.create(itemType, quantity);
        items.add(item);

        markAsUpdated();

        return item;
    }

    public ShoppingListItem get(ShoppingListItemId itemId) {
        return items
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .collect(onlyOptionalElement())
                .orElseThrow(() -> new ShoppingListItemNotFoundException(itemId));
    }

    public void remove(ShoppingListItemId listItemId) {
        ShoppingListItem listItem = items
                .stream()
                .filter(item -> item.getId().equals(listItemId))
                .collect(onlyOptionalElement())
                .orElseThrow(() -> new ShoppingListItemNotFoundException(listItemId));

        items.remove(listItem);
        removedItems.add(listItem.getId());
        markAsUpdated();
    }

    public void removeItemsInCart() {
        List<ShoppingListItem> cartItems = items
                .stream()
                .filter(ShoppingListItem::isInCart)
                .collect(toList());
        cartItems.forEach(item -> {
            items.remove(item);
            removedItems.add(item.getId());
        });

        if (!cartItems.isEmpty()) {
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
        return Collections.unmodifiableCollection(items);
    }

    public void loadFromDb(List<ItemType> itemTypes, Collection<ShoppingListItem> items) {
        this.itemTypes.clear();
        this.deletedItemTypes.clear();
        this.items.clear();
        this.removedItems.clear();
        itemTypes.forEach(itemType -> this.itemTypes.put(itemType.getId(), itemType));
        this.items.addAll(items);
    }
}
