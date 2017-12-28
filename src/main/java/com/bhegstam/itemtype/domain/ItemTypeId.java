package com.bhegstam.itemtype.domain;

import com.bhegstam.util.domain.Identifier;

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
