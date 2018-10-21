package com.bhegstam.shoppinglist.domain;

public class ItemTypeUsedInShoppingList extends RuntimeException {
    public ItemTypeUsedInShoppingList(ItemTypeId itemTypeId) {
        super(itemTypeId.getId());
    }
}
