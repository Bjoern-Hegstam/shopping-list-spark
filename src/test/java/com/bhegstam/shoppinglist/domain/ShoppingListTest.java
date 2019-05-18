package com.bhegstam.shoppinglist.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.bhegstam.shoppinglist.util.Matchers.isAfter;
import static com.bhegstam.shoppinglist.util.Matchers.isAtOrAfter;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ShoppingListTest {
    private static final ShoppingListId LIST_ID = new ShoppingListId();
    private static final String LIST_NAME = "LIST";
    private static final String ITEM_TYPE_NAME = "ITEM_TYPE";

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private Instant before;

    @Before
    public void setUp() {
        before = Instant.now();
    }

    @Test
    public void setNameOfNotPersistedShoppingList() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);

        assertThat(list.insertRequired(), is(true));
        assertThat(list.getCreatedAt(), isAtOrAfter(before));
        assertThat(list.getUpdatedAt(), is(list.getCreatedAt()));

        // when
        list.setName("New name");

        // then
        assertThat(list.insertRequired(), is(true));
        assertThat(list.getCreatedAt(), isAtOrAfter(before));
        assertThat(list.getUpdatedAt(), is(list.getCreatedAt()));
    }

    @Test
    public void setNameOfPersistedShoppingList() {
        // given
        Instant createdAt = before.minus(1, ChronoUnit.HOURS);
        ShoppingList list = ShoppingList.fromDb(LIST_ID.getId(), LIST_NAME, createdAt, createdAt);

        assertThat(list.getCreatedAt(), is(createdAt));
        assertThat(list.getUpdatedAt(), is(createdAt));

        // when
        list.setName("New name");

        // then
        assertThat(list.updateRequired(), is(true));
        assertThat(list.getCreatedAt(), is(createdAt));
        assertThat(list.getUpdatedAt(), isAfter(createdAt));
    }

    @Test
    public void addAndUpdateItemQuantity() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        ItemType itemType = new ItemType(ITEM_TYPE_NAME);

        // when item added for the first time
        ShoppingListItem listItem = list.add(itemType);

        // then
        errorCollector.checkThat(list.get(itemType.getId()).getQuantity(), is(1));
        errorCollector.checkThat(list.contains(itemType.getId()), is(true));

        // when item added again
        list.add(itemType);

        // then
        errorCollector.checkThat(list.get(itemType.getId()).getQuantity(), is(2));

        // when quantity updated via list item
        listItem.setQuantity(5);

        // then
        errorCollector.checkThat(list.get(itemType.getId()).getQuantity(), is(5));

        // when item removed
        list.remove(itemType.getId());

        // then
        errorCollector.checkThat(list.contains(itemType.getId()), is(false));
        errorCollector.checkThat(list.removedItemIds().contains(listItem.getId()), is(true));
    }

    @Test
    public void addItemToPersistedShoppingList() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        list.markAsPersisted();

        ItemType itemType = new ItemType(ITEM_TYPE_NAME);

        // when
        list.add(itemType);

        // then
        assertThat(list.updateRequired(), is(true));
    }

    @Test
    public void removeItemByItemTypeFromPersistedShoppingList() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        ItemType itemType = new ItemType(ITEM_TYPE_NAME);
        list.add(itemType);
        list.markAsPersisted();

        // when
        list.remove(itemType.getId());

        // then
        assertThat(list.updateRequired(), is(true));
    }

    @Test
    public void removeItemByIdFromPersistedShoppingList() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        ItemType itemType = new ItemType(ITEM_TYPE_NAME);
        ShoppingListItem listItem = list.add(itemType);
        list.markAsPersisted();

        // when
        list.remove(listItem.getId());

        // then
        assertThat(list.updateRequired(), is(true));
    }

    @Test
    public void accessByShoppingListItemId() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        ItemType itemType = new ItemType(ITEM_TYPE_NAME);

        // when
        ShoppingListItem listItem = list.add(itemType);

        // then
        ShoppingListItem retrievedListItem = list.get(listItem.getId());
        errorCollector.checkThat(retrievedListItem.getId(), is(listItem.getId()));

        // when
        list.remove(listItem.getId());

        // then
        errorCollector.checkThat(list.contains(itemType.getId()), is(false));
    }

    @Test
    public void cartManagement() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        ItemType itemTypeA = new ItemType(ITEM_TYPE_NAME + "_A");
        ItemType itemTypeB = new ItemType(ITEM_TYPE_NAME + "_B");

        ShoppingListItem itemA = list.add(itemTypeA);
        list.add(itemTypeB);

        // when
        itemA.setInCart(true);
        list.removeItemsInCart();

        // then
        errorCollector.checkThat(list.contains(itemTypeA.getId()), is(false));
        errorCollector.checkThat(list.removedItemIds().contains(itemA.getId()), is(true));

        errorCollector.checkThat(list.contains(itemTypeB.getId()), is(true));
    }

    @Test
    public void removeItemsInCartOfPersistedShoppingList() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        ItemType itemType = new ItemType(ITEM_TYPE_NAME);
        ShoppingListItem listItem = list.add(itemType);
        listItem.setInCart(true);

        list.markAsPersisted();

        // when
        list.removeItemsInCart();

        // then
        assertThat(list.updateRequired(), is(true));
    }

    @Test
    public void removeItemsInCartOfAlreadyEmptyPersistedShoppingList() {
        // given
        ShoppingList list = new ShoppingList(LIST_NAME);
        list.markAsPersisted();

        // when
        list.removeItemsInCart();

        // then
        assertThat(list.isPersisted(), is(true));
    }
}
