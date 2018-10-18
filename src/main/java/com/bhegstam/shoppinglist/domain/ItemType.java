package com.bhegstam.shoppinglist.domain;

public class ItemType extends Entity<ItemTypeId> {
    private final String name;

    public ItemType(String name) {
        this(new ItemTypeId(), name);
    }

    public ItemType(ItemTypeId id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
