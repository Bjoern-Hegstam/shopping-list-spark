package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.bhegstam.shoppinglist.util.Matchers.isAtOrAfter;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ItemTypeTest {
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void create() {
        Instant before = Instant.now();

        ItemType itemType = new ItemType("Foo");

        errorCollector.checkThat(itemType.getId(), notNullValue());
        errorCollector.checkThat(itemType.getName(), is("Foo"));
        errorCollector.checkThat(itemType.insertRequired(), is(true));
        errorCollector.checkThat(itemType.getCreatedAt(), isAtOrAfter(before));
        errorCollector.checkThat(itemType.getUpdatedAt(), is(itemType.getCreatedAt()));
    }

    @Test
    public void fromDb() {
        ItemTypeId id = new ItemTypeId();
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plus(1, ChronoUnit.HOURS);

        ItemType itemType = new ItemType(id, "Foo", createdAt, updatedAt, PersistenceStatus.PERSISTED);

        errorCollector.checkThat(itemType.getId(), is(id));
        errorCollector.checkThat(itemType.getName(), is("Foo"));
        errorCollector.checkThat(itemType.isPersisted(), is(true));
        errorCollector.checkThat(itemType.getCreatedAt(), is(createdAt));
        errorCollector.checkThat(itemType.getUpdatedAt(), is(updatedAt));
    }
}
