package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.*;

import java.util.List;

public class ShoppingListApplication {
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListApplication(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingListRepository.getShoppingLists();
    }

    public ShoppingList getShoppingList(ShoppingListId shoppingListId) {
        return shoppingListRepository.get(shoppingListId);
    }

    public ShoppingList createShoppingList(String name) {
        ShoppingList shoppingList = ShoppingList.create(name);
        shoppingListRepository.persist(shoppingList);
        return shoppingList;
    }

    public void updateShoppingList(ShoppingListId listId, String name) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        shoppingList.setName(name);
        shoppingListRepository.persist(shoppingList);
    }

    public void deleteShoppingList(ShoppingListId listId) {
        shoppingListRepository.delete(listId);
    }

    public ItemType createItemType(ShoppingListId listId, String name) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        ItemType itemType = shoppingList.addItemType(name);
        shoppingListRepository.persist(shoppingList);
        return itemType;
    }

    public List<ItemType> getItemTypes(ShoppingListId listId) {
        return shoppingListRepository.get(listId).getItemTypes();
    }

    public void deleteItemType(ShoppingListId listId, ItemTypeId itemTypeId) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        shoppingList.deleteItemType(itemTypeId);
        shoppingListRepository.persist(shoppingList);
    }

    public ShoppingListItem addItem(ShoppingListId listId, ItemTypeId itemTypeId, Integer quantity) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        ItemType itemType = shoppingList.getItemType(itemTypeId);

        ShoppingListItem listItem = shoppingList.addItem(itemType, quantity);
        shoppingListRepository.persist(shoppingList);

        return listItem;
    }

    public ShoppingListItem addItem(ShoppingListId listId, String itemTypeName, Integer quantity) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        ItemType itemType = shoppingList.addItemType(itemTypeName);

        ShoppingListItem listItem = shoppingList.addItem(itemType, quantity);
        shoppingListRepository.persist(shoppingList);

        return listItem;
    }

    public void updateItem(ShoppingListId listId, ShoppingListItemId listItemId, int quantity, boolean inCart) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);

        ShoppingListItem listItem = shoppingList.get(listItemId);

        listItem.setQuantity(quantity);
        listItem.setInCart(inCart);

        shoppingListRepository.persist(shoppingList);
    }

    public void deleteItem(ShoppingListId listId, ShoppingListItemId listItemId) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        shoppingList.remove(listItemId);
        shoppingListRepository.persist(shoppingList);
    }

    public void emptyCart(ShoppingListId listId) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        shoppingList.removeItemsInCart();
        shoppingListRepository.persist(shoppingList);
    }
}
