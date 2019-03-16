package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;

import java.time.Instant;

public class ItemType extends Entity<ItemTypeId> {
    private final String name;

    public ItemType(String name) {
        this(
                new ItemTypeId(),
                name,
                Instant.now(),
                null,
                PersistenceStatus.INSERT_REQUIRED
        );
    }

    public ItemType(
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
