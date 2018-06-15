package com.bhegstam.shoppinglist.domain;

import java.util.UUID;

public class ItemTypeId extends Identifier {
    public ItemTypeId() {
        super();
    }

    public ItemTypeId(UUID id) {
        super(id);
    }

    public static ItemTypeId fromString(String s) {
        return new ItemTypeId(UUID.fromString(s));
    }
}
