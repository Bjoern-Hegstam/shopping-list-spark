package com.bhegstam.shoppinglist.controller;

import com.bhegstam.itemtype.InMemoryItemTypeRepository;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import com.bhegstam.shoppinglist.InMemoryShoppingListRepository;
import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import org.junit.Before;
import org.junit.Test;

import static com.bhegstam.util.Mocks.mockRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ShoppingListApiControllerTest {

    private ShoppingListApiController controller;
    private ShoppingListRepository shoppingListRepository;
    private ItemTypeRepository itemTypeRepository;

    @Before
    public void setUp() {
        shoppingListRepository = new InMemoryShoppingListRepository();
        itemTypeRepository = new InMemoryItemTypeRepository();
        controller = new ShoppingListApiController(shoppingListRepository, itemTypeRepository);
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
        assertThat(response.getShoppingLists(), containsInAnyOrder(ShoppingListBean.fromShoppingList(shoppingList)));
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

    // TODO: Test remaining end points, change getShoppingLists to only return names and ids, not entire shopping lists, add endpoint to get specific shopping list
}