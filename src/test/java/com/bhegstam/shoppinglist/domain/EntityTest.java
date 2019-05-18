package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.core.Is.is;

public class EntityTest {
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void createWithoutUpdatedAt() {
        // given
        TestIdentifier id = new TestIdentifier();
        Instant createdAt = now();

        // when
        TestEntity entity = new TestEntity(id, createdAt, null, PersistenceStatus.INSERT_REQUIRED);

        // then
        errorCollector.checkThat(entity.getId(), is(id));
        errorCollector.checkThat(entity.getCreatedAt(), is(createdAt));
        errorCollector.checkThat(entity.getUpdatedAt(), is(createdAt));
        errorCollector.checkThat(entity.insertRequired(), is(true));
    }

    @Test
    public void createWithCustomUpdatedAt() {
        // given
        TestIdentifier id = new TestIdentifier();
        Instant createdAt = now();;
        Instant updatedAt = createdAt.plus(1, ChronoUnit.HOURS);

        // when
        TestEntity entity = new TestEntity(id, createdAt, updatedAt, PersistenceStatus.INSERT_REQUIRED);

        // then
        errorCollector.checkThat(entity.getId(), is(id));
        errorCollector.checkThat(entity.getCreatedAt(), is(createdAt));
        errorCollector.checkThat(entity.getUpdatedAt(), is(updatedAt));
        errorCollector.checkThat(entity.insertRequired(), is(true));
    }

    @Test
    public void markAsUpdatedWhenPersisted() {
        // given
        Instant createdAt = Instant.now().minus(1, ChronoUnit.HOURS);
        TestEntity entity = new TestEntity(new TestIdentifier(), createdAt, null, PersistenceStatus.PERSISTED);

        // when
        entity.markAsUpdated();

        // then
        errorCollector.checkThat(entity.updateRequired(), is(true));
        errorCollector.checkThat(entity.getUpdatedAt().isAfter(createdAt), is(true));
    }

    @Test
    public void markAsUpdatedWhenInsertRequired() {
        // given
        Instant createdAt = now().minus(1, ChronoUnit.HOURS);
        TestEntity entity = new TestEntity(new TestIdentifier(), createdAt, null, PersistenceStatus.INSERT_REQUIRED);

        // when
        entity.markAsUpdated();

        // then
        errorCollector.checkThat(entity.insertRequired(), is(true));
        errorCollector.checkThat(entity.getUpdatedAt(), is(createdAt));

    }

    @Test
    public void markAsUpdatedWhenUpdateRequired() {
        // given
        Instant createdAt = now().minus(1, ChronoUnit.HOURS);
        TestEntity entity = new TestEntity(new TestIdentifier(), createdAt, null, PersistenceStatus.UPDATED_REQUIRED);

        // when
        entity.markAsUpdated();

        // then
        errorCollector.checkThat(entity.updateRequired(), is(true));
        errorCollector.checkThat(entity.getUpdatedAt(), is(createdAt));
    }

    private Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }

    @Test
    public void equals() {
        EqualsVerifier
                .forClass(Entity.class)
                .withOnlyTheseFields("id")
                .verify();
    }
    private static final class TestIdentifier extends Identifier {

    }

    private static final class TestEntity extends Entity<TestIdentifier> {

        TestEntity(TestIdentifier id, Instant createdAt, Instant updatedAt, PersistenceStatus persistenceStatus) {
            super(id, createdAt, updatedAt, persistenceStatus);
        }
    }
}
