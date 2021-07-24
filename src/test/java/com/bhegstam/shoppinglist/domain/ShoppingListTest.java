package com.bhegstam.shoppinglist.domain;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.bhegstam.shoppinglist.util.Matchers.isAfter;
import static com.bhegstam.shoppinglist.util.Matchers.isAtOrAfter;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class ShoppingListTest {
    private static final WorkspaceId WORKSPACE_ID = new WorkspaceId();
    private static final ShoppingListId LIST_ID = new ShoppingListId();
    private static final String LIST_NAME = "LIST";
    private static final String ITEM_TYPE_NAME = "ITEM_TYPE";

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Instant before;

    @Before
    public void setUp() {
        before = Instant.now().truncatedTo(ChronoUnit.MICROS);
    }

    @Test
    public void setNameOfNotPersistedShoppingList() {
        // given
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);

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
        ShoppingList list = ShoppingList.fromDb(WORKSPACE_ID.getId(), LIST_ID.getId(), LIST_NAME, createdAt, createdAt);

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
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);
        ItemType itemType = ItemType.create(ITEM_TYPE_NAME);

        // when item added for the first time
        ShoppingListItem listItem = list.addItem(itemType);

        // then
        errorCollector.checkThat(list.get(listItem.getId()).getQuantity(), is(1));

        // when item added again
        ShoppingListItem listItem2 = list.addItem(itemType);

        // then
        errorCollector.checkThat(listItem2, not(is(listItem)));
        errorCollector.checkThat(list.get(listItem.getId()).getQuantity(), is(1));

        // when quantity updated via list item
        listItem.setQuantity(5);

        // then
        errorCollector.checkThat(list.get(listItem.getId()).getQuantity(), is(5));
        errorCollector.checkThat(list.get(listItem2.getId()).getQuantity(), is(1));

        // when item removed
        list.remove(listItem.getId());

        // then
        errorCollector.checkThat(list.removedItemIds().contains(listItem.getId()), is(true));
    }

    @Test
    public void addItemToPersistedShoppingList() {
        // given
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);
        list.markAsPersisted();

        ItemType itemType = ItemType.create(ITEM_TYPE_NAME);

        // when
        list.addItem(itemType);

        // then
        assertThat(list.updateRequired(), is(true));
    }

    @Test
    public void removeItemByIdFromPersistedShoppingList() {
        // given
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);
        ItemType itemType = ItemType.create(ITEM_TYPE_NAME);
        ShoppingListItem listItem = list.addItem(itemType);
        list.markAsPersisted();

        // when
        list.remove(listItem.getId());

        // then
        assertThat(list.updateRequired(), is(true));
    }

    @Test
    public void accessByShoppingListItemId() {
        // given
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);
        ItemType itemType = ItemType.create(ITEM_TYPE_NAME);

        // when
        ShoppingListItem listItem = list.addItem(itemType);

        // then
        ShoppingListItem retrievedListItem = list.get(listItem.getId());
        errorCollector.checkThat(retrievedListItem.getId(), is(listItem.getId()));

        // when
        list.remove(listItem.getId());

        // then
        expectedException.expect(ShoppingListItemNotFoundException.class);
        list.get(listItem.getId());
    }

    @Test
    public void cartManagement() {
        // given
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);
        ItemType itemTypeA = ItemType.create(ITEM_TYPE_NAME + "_A");
        ItemType itemTypeB = ItemType.create(ITEM_TYPE_NAME + "_B");

        ShoppingListItem itemA = list.addItem(itemTypeA);
        list.addItem(itemTypeB);

        // when
        itemA.setInCart(true);
        list.removeItemsInCart();

        // then
        errorCollector.checkThat(list.removedItemIds().contains(itemA.getId()), is(true));
    }

    @Test
    public void removeItemsInCartOfPersistedShoppingList() {
        // given
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);
        ItemType itemType = ItemType.create(ITEM_TYPE_NAME);
        ShoppingListItem listItem = list.addItem(itemType);
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
        ShoppingList list = ShoppingList.create(WORKSPACE_ID, LIST_NAME);
        list.markAsPersisted();

        // when
        list.removeItemsInCart();

        // then
        assertThat(list.isPersisted(), is(true));
    }
}
