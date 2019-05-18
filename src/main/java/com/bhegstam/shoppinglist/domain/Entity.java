package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public abstract class Entity<I extends Identifier> {
    private PersistenceStatus persistenceStatus;
    private final I id;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant updateAtOnFetch;

    Entity(I id, Instant createdAt, Instant updatedAt, PersistenceStatus persistenceStatus) {
        this.id = id;

        // Timestamps are truncated to ensure the timestamps in the database have the same
        // precision as the ones created by the application
        this.createdAt = truncateTimestamp(createdAt);
        this.updatedAt = truncateTimestamp(updatedAt != null ? updatedAt : createdAt);
        this.updateAtOnFetch = this.updatedAt;

        this.persistenceStatus = persistenceStatus;
    }

    private Instant truncateTimestamp(Instant instant) {
        return instant.truncatedTo(ChronoUnit.MICROS);
    }

    public I getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getUpdateAtOnFetch() {
        return updateAtOnFetch;
    }

    void markAsUpdated() {
        if (persistenceStatus != PersistenceStatus.PERSISTED) {
            // Already marked as needing insert/update
            return;
        }

        persistenceStatus = PersistenceStatus.UPDATED_REQUIRED;
        updatedAt = Instant.now();
    }

    public void markAsPersisted() {
        persistenceStatus = PersistenceStatus.PERSISTED;
        this.updateAtOnFetch = this.updatedAt;
    }

    public boolean insertRequired() {
        return persistenceStatus == PersistenceStatus.INSERT_REQUIRED;
    }

    public boolean updateRequired() {
        return persistenceStatus == PersistenceStatus.UPDATED_REQUIRED;
    }

    public boolean isPersisted() {
        return persistenceStatus == PersistenceStatus.PERSISTED;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }

        Entity<?> entity = (Entity<?>) o;

        return Objects.equals(id, entity.id);
    }

    @Override
    public final int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
