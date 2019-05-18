package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;

import java.time.Instant;

public class ItemType extends Entity<ItemTypeId> {
    private final String name;

    public static ItemType create(String name) {
        return new ItemType(
                new ItemTypeId(),
                name,
                Instant.now(),
                null,
                PersistenceStatus.INSERT_REQUIRED
        );
    }

    public static ItemType fromDb(
            ItemTypeId id,
            String name,
            Instant createdAt,
            Instant updatedAt,
            PersistenceStatus persistenceStatus
    ) {
        return new ItemType(id, name, createdAt, updatedAt, persistenceStatus);
    }


    private ItemType(
            ItemTypeId id,
            String name,
            Instant createdAt,
            Instant updatedAt,
            PersistenceStatus persistenceStatus
    ) {
        super(id, createdAt, updatedAt, persistenceStatus);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
