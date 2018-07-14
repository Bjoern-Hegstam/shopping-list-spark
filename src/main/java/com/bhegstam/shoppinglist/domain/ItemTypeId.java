package com.bhegstam.shoppinglist.domain;

public class ItemTypeId extends Identifier {
    public ItemTypeId() {
        super();
    }

    public ItemTypeId(String id) {
        super(id);
    }

    public static ItemTypeId parse(String id) {
        return new ItemTypeId(id);
    }
}
