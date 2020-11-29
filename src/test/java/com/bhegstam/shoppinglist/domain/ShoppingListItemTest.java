package com.bhegstam.shoppinglist.domain;

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
        itemType = ItemType.create("TEST_ITEM_TYPE");
    }

    @Test
    public void createNewItem_shouldBeMarkedAsNew() {
        // when
        ShoppingListItem item = ShoppingListItem.create(itemType, 0);

        // then
        errorCollector.checkThat(item.insertRequired(), is(true));

        // should still require insert after updating quantity or in cart status
        item.setQuantity(item.getQuantity() + 1);
        item.setInCart(!item.isInCart());

        errorCollector.checkThat(item.insertRequired(), is(true));
    }

    @Test
    public void setQuantity_shouldMarkItemAsUpdated() {
        // given
        ShoppingListItem item = ShoppingListItem.create(itemType, 0);
        item.markAsPersisted();

        // when
        item.setQuantity(item.getQuantity() + 1);

        // then
        errorCollector.checkThat(item.updateRequired(), is(true));
    }

    @Test
    public void setInCart_shouldMarkItemAsUpdated() {
        // given
        ShoppingListItem item = ShoppingListItem.create(itemType, 0);
        item.markAsPersisted();

        // when
        item.setInCart(!item.isInCart());

        // then
        errorCollector.checkThat(item.updateRequired(), is(true));
    }
}
