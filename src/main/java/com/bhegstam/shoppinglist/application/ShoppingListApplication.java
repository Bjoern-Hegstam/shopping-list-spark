package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.*;

import java.util.List;

public class ShoppingListApplication {
    private final WorkspaceRepository workspaceRepository;
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListApplication(WorkspaceRepository workspaceRepository, ShoppingListRepository shoppingListRepository) {
        this.workspaceRepository = workspaceRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    public List<ShoppingList> getShoppingLists(UserId userId) {
        return shoppingListRepository.getShoppingLists(userId);
    }

    public ShoppingList getShoppingList(UserId userId, ShoppingListId shoppingListId) {
        return shoppingListRepository.get(userId, shoppingListId);
    }

    public ShoppingList createShoppingList(UserId userId, String name) {
        final Workspace workspaceId = workspaceRepository.getDefaultWorkspace(userId);
        ShoppingList shoppingList = ShoppingList.create(workspaceId.getId(), name);
        shoppingListRepository.persist(userId, shoppingList);
        return shoppingList;
    }

    public void updateShoppingList(UserId userId, ShoppingListId listId, String name) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);
        shoppingList.setName(name);
        shoppingListRepository.persist(userId, shoppingList);
    }

    public void deleteShoppingList(UserId userId, ShoppingListId listId) {
        shoppingListRepository.delete(userId, listId);
    }

    public ItemType createItemType(UserId userId, ShoppingListId listId, String name) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);
        ItemType itemType = shoppingList.addItemType(name);
        shoppingListRepository.persist(userId, shoppingList);
        return itemType;
    }

    public List<ItemType> getItemTypes(UserId userId, ShoppingListId listId) {
        return shoppingListRepository.get(userId, listId).getItemTypes();
    }

    public void deleteItemType(UserId userId, ShoppingListId listId, ItemTypeId itemTypeId) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);
        shoppingList.deleteItemType(itemTypeId);
        shoppingListRepository.persist(userId, shoppingList);
    }

    public ShoppingListItem addItem(UserId userId, ShoppingListId listId, ItemTypeId itemTypeId, Integer quantity) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);
        ItemType itemType = shoppingList.getItemType(itemTypeId);

        ShoppingListItem listItem = shoppingList.addItem(itemType, quantity);
        shoppingListRepository.persist(userId, shoppingList);

        return listItem;
    }

    public ShoppingListItem addItem(UserId userId, ShoppingListId listId, String itemTypeName, Integer quantity) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);
        ItemType itemType = shoppingList.addItemType(itemTypeName);

        ShoppingListItem listItem = shoppingList.addItem(itemType, quantity);
        shoppingListRepository.persist(userId, shoppingList);

        return listItem;
    }

    public void updateItem(UserId userId, ShoppingListId listId, ShoppingListItemId listItemId, int quantity, boolean inCart) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);

        ShoppingListItem listItem = shoppingList.get(listItemId);

        listItem.setQuantity(quantity);
        listItem.setInCart(inCart);

        shoppingListRepository.persist(userId, shoppingList);
    }

    public void deleteItem(UserId userId, ShoppingListId listId, ShoppingListItemId listItemId) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);
        shoppingList.remove(listItemId);
        shoppingListRepository.persist(userId, shoppingList);
    }

    public void emptyCart(UserId userId, ShoppingListId listId) {
        ShoppingList shoppingList = shoppingListRepository.get(userId, listId);
        shoppingList.removeItemsInCart();
        shoppingListRepository.persist(userId, shoppingList);
    }
}
