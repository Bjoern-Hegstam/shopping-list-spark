package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.is;

public class ShoppingListItemTest {
    private ItemType itemType;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        itemType = new ItemType("TEST_ITEM_TYPE");
    }

    @Test
    public void createNewItem_shouldBeMarkedAsNew() {
        // when
        ShoppingListItem item = new ShoppingListItem(itemType);

        // then
        errorCollector.checkThat(item.getPersistenceStatus(), is(PersistenceStatus.INSERT_REQUIRED));

        // should still require insert after updating quantity or in cart status
        item.setQuantity(item.getQuantity() + 1);
        item.setInCart(!item.isInCart());

        errorCollector.checkThat(item.getPersistenceStatus(), is(PersistenceStatus.INSERT_REQUIRED));
    }

    @Test
    public void setQuantity_shouldMarkItemAsUpdated() {
        // given
        ShoppingListItem item = new ShoppingListItem(itemType);
        item.setPersistenceStatus(PersistenceStatus.PERSISTED);

        // when
        item.setQuantity(item.getQuantity() + 1);

        // then
        errorCollector.checkThat(item.getPersistenceStatus(), is(PersistenceStatus.UPDATED_REQUIRED));
    }

    @Test
    public void setInCart_shouldMarkItemAsUpdated() {
        // given
        ShoppingListItem item = new ShoppingListItem(itemType);
        item.setPersistenceStatus(PersistenceStatus.PERSISTED);

        // when
        item.setInCart(!item.isInCart());

        // then
        errorCollector.checkThat(item.getPersistenceStatus(), is(PersistenceStatus.UPDATED_REQUIRED));
    }
}