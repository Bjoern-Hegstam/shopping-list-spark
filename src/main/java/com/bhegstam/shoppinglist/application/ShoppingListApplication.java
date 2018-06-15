package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.port.rest.ShoppingListItemBean;

import java.util.List;
import java.util.Optional;

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

    public ShoppingList get(ShoppingListId shoppingListId) {
        return shoppingListRepository.get(shoppingListId);
    }

    public ShoppingList createShoppingList(String name) {
        return shoppingListRepository.createShoppingList(name);
    }

    public ShoppingListItem addItem(ShoppingListId listId, ShoppingListItemBean itemBean, ItemTypeId itemTypeId) {
        ItemType itemType = itemTypeRepository.get(itemTypeId);

        ShoppingList shoppingList = shoppingListRepository.get(listId);
        ShoppingListItem listItem = shoppingList.add(itemType);
        listItem.setQuantity(itemBean.getQuantity());

        shoppingListRepository.update(shoppingList);

        return listItem;
    }

    public void updateItem(ShoppingListId listId, ShoppingListItemId listItemId, ShoppingListItemBean itemBean) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);

        ShoppingListItem listItem = shoppingList.get(listItemId);

        Optional.ofNullable(itemBean.getQuantity()).ifPresent(listItem::setQuantity);
        Optional.ofNullable(itemBean.getInCart()).ifPresent(listItem::setInCart);

        shoppingListRepository.update(shoppingList);
    }

    public void deleteItem(ShoppingListId listId, ShoppingListItemId listItemId) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        shoppingList.remove(listItemId);
        shoppingListRepository.update(shoppingList);
    }

    public void emptyCart(ShoppingListId listId) {
        ShoppingList shoppingList = shoppingListRepository.get(listId);
        shoppingList.removeItemsInCart();
        shoppingListRepository.update(shoppingList);
    }
}
