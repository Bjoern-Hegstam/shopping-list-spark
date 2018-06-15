package com.bhegstam.shoppinglist.domain;

public class ItemType {
    private final ItemTypeId id;
    private final String name;

    public ItemType(ItemTypeId id, String name) {
        this.id = id;
        this.name = name;
    }

    public ItemTypeId getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
