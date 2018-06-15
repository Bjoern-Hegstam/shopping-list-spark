package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.persistence.InMemoryItemTypeRepository;
import com.bhegstam.shoppinglist.persistence.InMemoryShoppingListRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ShoppingListTest {
    private ShoppingListRepository shoppingListRepository;
    private InMemoryItemTypeRepository itemTypeRepository;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        shoppingListRepository = new InMemoryShoppingListRepository();
        itemTypeRepository = new InMemoryItemTypeRepository();
    }

    @Test
    public void createNewShoppingList() {
        // given
        String listName = "TEST_LIST";

        // when created
        ShoppingList shoppingList = shoppingListRepository.createShoppingList(listName);

        // then
        errorCollector.checkThat(shoppingList.getName(), is(listName));
        errorCollector.checkThat(shoppingList.getId(), is(notNullValue()));

        // when retrieved from shoppingListRepository
        ShoppingList listFromRepo = shoppingListRepository.get(shoppingList.getId());

        // then
        errorCollector.checkThat(listFromRepo, is(shoppingList));
    }

    @Test
    public void addAndUpdateItemQuantity() {
        // given
        ShoppingList list = shoppingListRepository.createShoppingList("LIST");
        ItemType itemType = itemTypeRepository.createItemType("ITEM_TYPE");

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
        ShoppingList list = shoppingListRepository.createShoppingList("LIST");
        ItemType itemType = itemTypeRepository.createItemType("ITEM_TYPE");


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
        ShoppingList list = shoppingListRepository.createShoppingList("LIST");
        ItemType itemTypeA = itemTypeRepository.createItemType("ITEM_TYPE_A");
        ItemType itemTypeB = itemTypeRepository.createItemType("ITEM_TYPE_B");

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
