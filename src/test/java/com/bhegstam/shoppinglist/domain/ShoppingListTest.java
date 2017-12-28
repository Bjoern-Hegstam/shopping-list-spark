package com.bhegstam.shoppinglist.domain;

import com.bhegstam.itemtype.InMemoryItemTypeRepository;
import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.shoppinglist.InMemoryShoppingListRepository;
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
        ShoppingListItem listItem = list.add(itemType.getId());

        // then
        errorCollector.checkThat(list.get(itemType.getId()).getQuantity(), is(1));
        errorCollector.checkThat(list.contains(itemType.getId()), is(true));

        // when item added again
        list.add(itemType.getId());

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
    }
}
