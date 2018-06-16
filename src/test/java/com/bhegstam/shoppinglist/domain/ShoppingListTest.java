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
        ShoppingList shoppingList = new ShoppingList(listName);

        // when created
        shoppingListRepository.persist(shoppingList);

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
        ShoppingList list = new ShoppingList("LIST");

        shoppingListRepository.persist(list);
        ItemType itemType = new ItemType("ITEM_TYPE");
        itemTypeRepository.createItemType(itemType);

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
        shoppingListRepository.persist(list);

        ItemType itemType = new ItemType("ITEM_TYPE");
        itemTypeRepository.createItemType(itemType);


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
        shoppingListRepository.persist(list);

        ItemType itemTypeA = new ItemType("ITEM_TYPE_A");
        itemTypeRepository.createItemType(itemTypeA);

        ItemType itemTypeB = new ItemType("ITEM_TYPE_B");
        itemTypeRepository.createItemType(itemTypeB);

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
