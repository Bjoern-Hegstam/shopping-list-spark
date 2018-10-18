package com.bhegstam.shoppinglist.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.is;

public class ShoppingListTest {
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void addAndUpdateItemQuantity() {
        // given
        ShoppingList list = new ShoppingList("LIST");
        ItemType itemType = new ItemType("ITEM_TYPE");

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
    public void accessByShoppingListItemId() {
        // given
        ShoppingList list = new ShoppingList("LIST");
        ItemType itemType = new ItemType("ITEM_TYPE");

        // when
        ShoppingListItem listItem = list.add(itemType);

        // then
        ShoppingListItem retrievedListItem = list.get(listItem.getId());
        errorCollector.checkThat(retrievedListItem.getId(), is(listItem.getId()));

        // when
        list.remove(listItem.getId());

        //
        errorCollector.checkThat(list.contains(itemType.getId()), is(false));
    }

    @Test
    public void cartManagement() {
        // given
        ShoppingList list = new ShoppingList("LIST");
        ItemType itemTypeA = new ItemType("ITEM_TYPE_A");
        ItemType itemTypeB = new ItemType("ITEM_TYPE_B");

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
}
