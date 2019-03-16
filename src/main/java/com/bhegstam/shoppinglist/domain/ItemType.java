package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ItemType extends Entity<ItemTypeId> {
    private final String name;

    public ItemType(String name) {
        this(new ItemTypeId(), name, PersistenceStatus.INSERT_REQUIRED);
    }

    public ItemType(ItemTypeId id, String name, PersistenceStatus persistenceStatus) {
        super(
                id,
                LocalDateTime.now(ZoneOffset.UTC),
                LocalDateTime.now(ZoneOffset.UTC),
                persistenceStatus
        );
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
