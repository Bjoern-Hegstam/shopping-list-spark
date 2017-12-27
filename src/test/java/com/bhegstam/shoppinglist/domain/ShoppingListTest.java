package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.InMemoryShoppingListRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ShoppingListTest {
    private ShoppingListRepository repository;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        repository = new InMemoryShoppingListRepository();
    }

    @Test
    public void createNewShoppingList() {
        // given
        String listName = "TEST_LIST";

        // when created
        ShoppingList shoppingList = repository.createShoppingList(listName);

        // then
        errorCollector.checkThat(shoppingList.getName(), is(listName));
        errorCollector.checkThat(shoppingList.getId(), is(notNullValue()));

        // when retrieved from repository
        ShoppingList listFromRepo = repository.get(shoppingList.getId());

        // then
        errorCollector.checkThat(listFromRepo, is(shoppingList));
    }
}
