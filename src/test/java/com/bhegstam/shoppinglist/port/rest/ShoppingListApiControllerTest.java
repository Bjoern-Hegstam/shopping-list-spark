package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.application.ShoppingListApplication;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.persistence.InMemoryItemTypeRepository;
import com.bhegstam.shoppinglist.persistence.InMemoryShoppingListRepository;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.UUID;

import static com.bhegstam.util.Matchers.isPresentAnd;
import static com.bhegstam.util.Mocks.mockRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ShoppingListApiControllerTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private ShoppingListApiController controller;
    private ShoppingListRepository shoppingListRepository;
    private ItemTypeRepository itemTypeRepository;

    @Before
    public void setUp() {
        shoppingListRepository = new InMemoryShoppingListRepository();
        itemTypeRepository = new InMemoryItemTypeRepository();
        controller = new ShoppingListApiController(new ShoppingListApplication(shoppingListRepository, itemTypeRepository));
    }

    @Test
    public void getShoppingLists_noListsExist() {
        // given

        // when
        Result result = controller.getShoppingLists(mockRequest());

        // then
        GetShoppingListsResponse response = (GetShoppingListsResponse) result.getResponsePayload();
        assertThat(response.getShoppingLists().size(), is(0));
    }

    @Test
    public void getShoppingLists_existsLists() {
        // given
        ShoppingList shoppingList = shoppingListRepository.createShoppingList("Test list");

        // when
        Result result = controller.getShoppingLists(mockRequest());

        // then
        GetShoppingListsResponse response = (GetShoppingListsResponse) result.getResponsePayload();
        assertThat(response.getShoppingLists(), containsInAnyOrder(new ShoppingListInfo(shoppingList)));
    }

    @Test
    public void postShoppingList() {
        // given
        Request request = mockRequest();
        when(request.body()).thenReturn("{ \"name\": \"Test list\" }");

        // when
        Result result = controller.postShoppingList(request);

        // then
        CreateShoppingListResponse response = (CreateShoppingListResponse) result.getResponsePayload();
        assertThat(response.getId(), notNullValue());
    }

    @Test
    public void getShoppingList() {
        // given
        ItemType itemType = itemTypeRepository.createItemType("itemType");

        ShoppingList shoppingList = shoppingListRepository.createShoppingList("Foo");
        ShoppingListItem item = shoppingList.add(itemType);

        shoppingListRepository.update(shoppingList);

        Request request = mockRequest();
        when(request.queryParams("shoppingListId")).thenReturn(shoppingList.getId().getId().toString());

        // when
        Result result = controller.getShoppingList(request);

        // then
        GetShoppingListResponse response = (GetShoppingListResponse) result.getResponsePayload();
        errorCollector.checkThat(response.getId(), is(shoppingList.getId().getId().toString()));
        errorCollector.checkThat(response.getName(), is("Foo"));
        errorCollector.checkThat(response.getItems(), containsInAnyOrder(new ShoppingListItemBean(item)));
    }

    @Test
    public void getShoppingList_unknownId() {
        // given
        Request request = mockRequest();
        when(request.queryParams("shoppingListId")).thenReturn(UUID.randomUUID().toString());

        // when
        Result result = controller.getShoppingList(request);

        // then
        assertThat(result.getStatusCode(), isPresentAnd(is(HttpStatus.BAD_REQUEST_400)));
    }

    // TODO: Test remaining end points, add endpoint to get specific shopping list
}