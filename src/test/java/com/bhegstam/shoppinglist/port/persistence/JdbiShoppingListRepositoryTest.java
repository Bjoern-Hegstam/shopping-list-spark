package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.List;

import static com.bhegstam.shoppinglist.util.Matchers.isEmpty;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JdbiShoppingListRepositoryTest {
    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ShoppingListRepository shoppingListRepository;
    private ItemType itemType1;
    private ItemType itemType2;

    @Before
    public void setUp() {
        shoppingListRepository = testDatabaseSetup.getRepositoryFactory().createShoppingListRepository();

        ItemTypeRepository itemTypeRepository = testDatabaseSetup.getRepositoryFactory().createItemTypeRepository();

        itemType1 = ItemType.create("itemType1");
        itemTypeRepository.add(itemType1);

        itemType2 = ItemType.create("itemType2");
        itemTypeRepository.add(itemType2);
    }

    @Test(expected = ShoppingListNotFoundException.class)
    public void get_listNotFound() {
        shoppingListRepository.get(ShoppingListId.fromString("a1f0b13b-41a2-4a47-96d5-10e4c2f7de67"));
    }

    @Test
    public void persist_emptyList() {
        // given
        ShoppingList shoppingList = ShoppingList.create("Foo");

        // when
        shoppingListRepository.persist(shoppingList);

        // then
        ShoppingList persistedList = shoppingListRepository.get(shoppingList.getId());
        errorCollector.checkThat(persistedList.getName(), is("Foo"));
        errorCollector.checkThat(persistedList.getItems(), isEmpty());
        errorCollector.checkThat(persistedList.isPersisted(), is(true));
    }

    @Test
    public void persist_updateNameOfList() {
        // given
        ShoppingList shoppingList = ShoppingList.create("Foo");
        shoppingListRepository.persist(shoppingList);

        // when
        shoppingList.setName("Bar");
        shoppingListRepository.persist(shoppingList);

        // then
        ShoppingList persistedList = shoppingListRepository.get(shoppingList.getId());
        assertThat(persistedList.getName(), is("Bar"));
        assertTrue(persistedList.getUpdatedAt().isAfter(persistedList.getCreatedAt()));
    }

    @Test
    public void persist_itemsAddedBeforePersist() {
        // create new list with items
        ShoppingList shoppingList = ShoppingList.create("Foo");
        ShoppingListItem item1 = shoppingList.add(itemType1);

        ShoppingListItem item2 = shoppingList.add(itemType2);
        item2.setQuantity(5);
        item2.setInCart(true);

        // when
        shoppingListRepository.persist(shoppingList);

        // then items should be persisted
        ShoppingList persistedList = shoppingListRepository.get(shoppingList.getId());

        assertThat(persistedList.isPersisted(), is(true));
        assertThat(persistedList.getItems(), containsInAnyOrder(item1, item2));

        ShoppingListItem persistedItem1 = persistedList.get(itemType1.getId());
        errorCollector.checkThat(persistedItem1.getQuantity(), is(1));
        errorCollector.checkThat(persistedItem1.isInCart(), is(false));
        errorCollector.checkThat(persistedItem1.isPersisted(), is(true));

        ShoppingListItem persistedItem2 = persistedList.get(itemType2.getId());
        errorCollector.checkThat(persistedItem2.getQuantity(), is(5));
        errorCollector.checkThat(persistedItem2.isInCart(), is(true));
        errorCollector.checkThat(persistedItem2.isPersisted(), is(true));
    }


    @Test
    public void persist_addItemsToPersistedList() {
        // given
        ShoppingList shoppingList = ShoppingList.create("Foo");
        shoppingListRepository.persist(shoppingList);

        // when
        ShoppingListItem item1 = shoppingList.add(itemType1);
        shoppingListRepository.persist(shoppingList);

        // then
        ShoppingList persistedList = shoppingListRepository.get(shoppingList.getId());
        assertThat(persistedList.getItems(), contains(item1));
    }

    @Test
    public void persist_updateItemInPersistedList() {
        // given
        ShoppingList shoppingList = ShoppingList.create("Foo");
        ShoppingListItem item1 = shoppingList.add(itemType1);
        shoppingListRepository.persist(shoppingList);

        // when
        item1.setQuantity(5);
        shoppingListRepository.persist(shoppingList);

        // then
        ShoppingList persistedList = shoppingListRepository.get(shoppingList.getId());
        assertThat(persistedList.getItems(), contains(item1));
        assertThat(persistedList.get(item1.getId()).getQuantity(), is(5));

    }

    @Test
    public void persist_itemDeletedFromList() {
        // given
        ShoppingList shoppingList = ShoppingList.create("Foo");
        ShoppingListItem item1 = shoppingList.add(itemType1);
        ShoppingListItem item2 = shoppingList.add(itemType2);

        shoppingListRepository.persist(shoppingList);

        // when
        shoppingList.remove(item1.getId());
        shoppingListRepository.persist(shoppingList);

        // then
        assertThat(shoppingList.getItems(), contains(item2));
        assertTrue(shoppingList.removedItemIds().isEmpty());

        ShoppingList persistedList = shoppingListRepository.get(shoppingList.getId());
        assertThat(persistedList.getItems(), contains(item2));
    }

    @Test
    public void getShoppingLists_emptyDb() {
        // when
        List<ShoppingList> lists = shoppingListRepository.getShoppingLists();

        // then
        assertThat(lists, isEmpty());
    }

    @Test
    public void getShoppingLists() {
        // given
        ShoppingList shoppingList1 = ShoppingList.create("Foo");
        ShoppingListItem item1 = shoppingList1.add(itemType1);
        shoppingListRepository.persist(shoppingList1);

        ShoppingList shoppingList2 = ShoppingList.create("Bar");
        shoppingListRepository.persist(shoppingList2);

        // when
        List<ShoppingList> lists = shoppingListRepository.getShoppingLists();

        // then
        assertThat(lists, contains(shoppingList2, shoppingList1));
        assertThat(lists.get(1).getItems(), contains(item1));
    }

    @Test
    public void delete() {
        // given
        ShoppingList shoppingList1 = ShoppingList.create("Foo");
        ShoppingList shoppingList2 = ShoppingList.create("Bar");
        shoppingListRepository.persist(shoppingList1);
        shoppingListRepository.persist(shoppingList2);

        // when
        shoppingListRepository.delete(shoppingList1.getId());

        // then
        assertThat(shoppingListRepository.getShoppingLists(), contains(shoppingList2));
    }

    @Test
    public void delete_listDoesNotExist() {
        shoppingListRepository.delete(new ShoppingListId());
    }

    @Test
    public void delete_listHasItems() {
        // given
        ShoppingList shoppingList = ShoppingList.create("Foo");
        shoppingList.add(itemType1);
        shoppingListRepository.persist(shoppingList);

        // then
        expectedException.expect(ShoppingListDeleteNotAllowedException.class);

        // when
        shoppingListRepository.delete(shoppingList.getId());
    }
}
