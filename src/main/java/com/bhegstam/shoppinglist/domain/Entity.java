package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public abstract class Entity<I extends Identifier> {
    private PersistenceStatus persistenceStatus;
    private final I id;
    private final LocalDateTime createdAtUtc;
    private LocalDateTime updatedAtUtc;

    Entity(I id, LocalDateTime createdAtUtc, LocalDateTime updatedAtUtc, PersistenceStatus persistenceStatus) {
        this.id = id;
        this.createdAtUtc = createdAtUtc;
        this.updatedAtUtc = updatedAtUtc;
        this.persistenceStatus = persistenceStatus;
    }

    public I getId() {
        return id;
    }

    void markAsUpdated() {
        if (persistenceStatus != PersistenceStatus.PERSISTED) {
            // Already marked as needing insert/update
            return;
        }

        persistenceStatus = PersistenceStatus.UPDATED_REQUIRED;
        updatedAtUtc = LocalDateTime.now(ZoneOffset.UTC);
    }

    public PersistenceStatus getPersistenceStatus() {
        return persistenceStatus;
    }

    public void setPersistenceStatus(PersistenceStatus persistenceStatus) {
        this.persistenceStatus = persistenceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Entity<?> entity = (Entity<?>) o;

        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
