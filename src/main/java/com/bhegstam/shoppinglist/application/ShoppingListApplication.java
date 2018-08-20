package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.*;

import java.util.List;

public class ShoppingListApplication {
    private final ShoppingListRepository shoppingListRepository;
    private final ItemTypeRepository itemTypeRepository;

    public ShoppingListApplication(ShoppingListRepository shoppingListRepository, ItemTypeRepository itemTypeRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.itemTypeRepository = itemTypeRepository;
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingListRepository.getShoppingLists();
    }

    public ShoppingList getShoppingList(ShoppingListId shoppingListId) {
        return shoppingListRepository.get(shoppingListId);
    }

    public ShoppingList createShoppingList(String name) {
        ShoppingList shoppingList = new ShoppingList(name);
        shoppingListRepository.persist(shoppingList);
        return shoppingList;
    }

    public void deleteShoppingList(ShoppingListId listId) {
        shoppingListRepository.delete(listId);
    }

    public ShoppingListItem addItem(ShoppingListId listId, ItemTypeId itemTypeId, Integer quantity) {
        ItemType itemType = itemTypeRepository.get(itemTypeId);

        ShoppingList shoppingList = shoppingListRepository.get(listId);
        ShoppingListItem listItem = shoppingList.add(itemType);
        listItem.setQuantity(quantity);

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
