package com.bhegstam.shoppinglist.domain;

public class ItemTypeUsedInShoppingListException extends RuntimeException {
    public ItemTypeUsedInShoppingListException(ItemTypeId itemTypeId) {
        super(itemTypeId.getId());
    }
}
