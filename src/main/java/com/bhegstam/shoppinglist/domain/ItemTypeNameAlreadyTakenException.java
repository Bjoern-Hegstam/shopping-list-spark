package com.bhegstam.shoppinglist.domain;

public class ItemTypeNameAlreadyTakenException extends RuntimeException {
    private ItemType itemType;

    public ItemTypeNameAlreadyTakenException(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
