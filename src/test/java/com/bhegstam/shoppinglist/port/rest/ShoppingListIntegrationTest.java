package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.util.DropwizardAppRuleFactory;
import com.bhegstam.shoppinglist.util.TestData;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ShoppingListIntegrationTest {
    private static final String LIST_NAME = "Test list";
    private static final String ITEM_TYPE_NAME = "Apples";
    private static final String INVALID_ID = "invalid-id";

    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    private final JsonMapper jsonMapper = new JsonMapper();
    private ShoppingListRepository shoppingListRepository;
    private ShoppingList shoppingList;
    private ItemType itemType;
    private ShoppingListApi api;

    @Before
    public void setUp() throws JoseException {
        String serviceUrl = "http://localhost:" + service.getLocalPort() + "/api/";
        String token = TokenGenerator.generate(TestData.ADMIN, service.getConfiguration().getJwtTokenSecret());
        api = new ShoppingListApi(token, serviceUrl);

        shoppingListRepository = testDatabaseSetup.getRepositoryFactory().createShoppingListRepository();

        shoppingList = new ShoppingList(LIST_NAME);

        itemType = new ItemType(ITEM_TYPE_NAME);
        testDatabaseSetup
                .getRepositoryFactory()
                .createItemTypeRepository()
                .add(itemType);
    }

    @Test
    public void getShoppingLists_noListsExist() {
        Response response = api.getShoppingLists();
        ResponseTestUtil.assertResponseStatus(response, OK);

        JsonNode responseJson = jsonMapper.read(response);
        ArrayNode shoppingLists = (ArrayNode) responseJson.findValue("shoppingLists");

        assertThat(shoppingLists.size(), is(0));
    }

    @Test
    public void getShoppingLists_existsLists() {
        // given
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.getShoppingLists();
        ResponseTestUtil.assertResponseStatus(response, OK);

        // then
        JsonNode responseJson = jsonMapper.read(response);

        JsonNode shoppingLists = responseJson.findValue("shoppingLists");
        assertThat(shoppingLists.size(), is(1));

        JsonNode shoppingListJson = shoppingLists.get(0);
        assertThat(shoppingListJson.findValue("id").asText(), is(shoppingList.getId().getId()));
        assertThat(shoppingListJson.findValue("name").asText(), is(shoppingList.getName()));
    }

    @Test
    public void createAndGetShoppingList() {
        // Create
        Response createResponse = api.addShoppingList("{ \"name\": \"Test list\" }");
        ResponseTestUtil.assertResponseStatus(createResponse, CREATED);

        JsonNode createResponseJson = jsonMapper.read(createResponse);
        String listId = createResponseJson.findValue("id").asText();
        assertThat(listId, notNullValue());

        // Get list that should be empty
        Response getNewListResponse = api.getShoppingList(listId);
        ResponseTestUtil.assertResponseStatus(getNewListResponse, OK);

        JsonNode getNewListResponseJson = jsonMapper.read(getNewListResponse);
        assertThat(getNewListResponseJson.findValue("id").asText(), is(listId));
        assertThat(getNewListResponseJson.findValue("name").asText(), is(LIST_NAME));
        assertThat(getNewListResponseJson.findValue("items").size(), is(0));
    }

    @Test
    public void getShoppingList_invalidId() {
        // when
        Response response = api.getShoppingList(INVALID_ID);

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void getShoppingList_unknownId() {
        // when
        Response response = api.getShoppingList("6b837531-7773-4a20-ad17-93d018a65a47");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void addItemToEmptyShoppingList() {
        // given
        shoppingListRepository.add(shoppingList);
        ShoppingListId listId = shoppingList.getId();

        // Add item to list
        Response addItemResponse = api.addShoppingListItem(listId.getId(), "{ \"itemTypeId\": \"" + itemType.getId().getId() + "\", \"quantity\": 3 }");

        ResponseTestUtil.assertResponseStatus(addItemResponse, CREATED);

        // Get and verify list
        Response getListResponse = api.getShoppingList(listId.getId());
        ResponseTestUtil.assertResponseStatus(getListResponse, OK);

        JsonNode getListResponseJson = jsonMapper.read(getListResponse);
        assertThat(getListResponseJson.findValue("id").asText(), is(listId.getId()));
        assertThat(getListResponseJson.findValue("name").asText(), is(LIST_NAME));

        JsonNode items = getListResponseJson.findValue("items");
        assertThat(items.size(), is(1));

        JsonNode itemJson = items.get(0);
        assertThat(itemJson.findValue("id").asText(), notNullValue());
        assertThat(itemJson.findValue("quantity").asText(), is("3"));
        assertThat(itemJson.findValue("inCart").asText(), is("false"));

        JsonNode itemTypeJson = itemJson.findValue("itemType");
        assertThat(itemTypeJson.findValue("id").asText(), is(itemType.getId().getId()));
        assertThat(itemTypeJson.findValue("name").asText(), is(ITEM_TYPE_NAME));
    }

    @Test
    public void addItem_invalidListId() {
        // when
        Response response = api.addShoppingListItem(INVALID_ID, "{ \"itemTypeId\": \"" + itemType.getId().getId() + "\", \"quantity\": 1 }");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void addItem_unknownListId() {
        // when
        Response response = api.addShoppingListItem("26490c45-3ef2-422b-bf28-de14c04fce61", "{ \"itemTypeId\": \"" + itemType.getId().getId() + "\", \"quantity\": 1 }");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void addItem_invalidItemTypeId() {
        // given
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.addShoppingListItem(shoppingList.getId().getId(), "{ \"itemTypeId\": \"" + INVALID_ID + "\", \"quantity\": 3 }");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void addItem_unknownItemTypeId() {
        // given
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.addShoppingListItem(shoppingList.getId().getId(), "{ \"itemTypeId\": \"d432242b-94f8-4d6a-b930-d3603485d470\", \"quantity\": 3 }");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void addItem_missingItemTypeId() {
        // given
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.addShoppingListItem(shoppingList.getId().getId(), "{ \"quantity\": 3 }");

        // then
        ResponseTestUtil.assertResponseStatus(response, ResponseTestUtil.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void addItem_missingQuantityId() {
        // given
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.addShoppingListItem(shoppingList.getId().getId(), "{ \"itemTypeId\": \"" + itemType.getId().getId() + "\" }");

        // then
        ResponseTestUtil.assertResponseStatus(response, ResponseTestUtil.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void updateItem() {
        // given
        ShoppingListItem listItem = shoppingList.add(itemType);
        shoppingListRepository.add(shoppingList);
        ShoppingListId listId = shoppingList.getId();

        // when
        Response updateItemResponse = api.updateShoppingListItem(listId.getId(), listItem.getId().getId(), "{ \"quantity\": 3, \"inCart\": \"true\"}");
        ResponseTestUtil.assertResponseStatus(updateItemResponse, NO_CONTENT);

        // then
        Response getListResponse = api.getShoppingList(listId.getId());
        ResponseTestUtil.assertResponseStatus(getListResponse, OK);

        JsonNode getListResponseJson = jsonMapper.read(getListResponse);
        assertThat(getListResponseJson.findValue("id").asText(), is(listId.getId()));
        assertThat(getListResponseJson.findValue("name").asText(), is(LIST_NAME));

        JsonNode items = getListResponseJson.findValue("items");
        assertThat(items.size(), is(1));

        JsonNode itemJson = items.get(0);
        assertThat(itemJson.findValue("id").asText(), is(listItem.getId().getId()));
        assertThat(itemJson.findValue("quantity").asText(), is("3"));
        assertThat(itemJson.findValue("inCart").asText(), is("true"));

        JsonNode itemTypeJson = itemJson.findValue("itemType");
        assertThat(itemTypeJson.findValue("id").asText(), is(itemType.getId().getId()));
        assertThat(itemTypeJson.findValue("name").asText(), is(ITEM_TYPE_NAME));
    }

    @Test
    public void updateItem_invalidListId() {
        // when
        Response response = api.updateShoppingListItem(INVALID_ID, "ff428927-8618-4f3d-b1a8-a38a82c42c38", "{}");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void updateItem_unknownListId() {
        // when
        Response response = api.updateShoppingListItem("26490c45-3ef2-422b-bf28-de14c04fce61", "ff428927-8618-4f3d-b1a8-a38a82c42c38", "{}");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void updateItem_invalidListItemId() {
        // when
        Response response = api.updateShoppingListItem("26490c45-3ef2-422b-bf28-de14c04fce61", INVALID_ID, "{}");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void updateItem_unknownListItemId() {
        // when
        Response response = api.updateShoppingListItem(shoppingList.getId().getId(), "ff428927-8618-4f3d-b1a8-a38a82c42c38", "{}");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void deleteItem() {
        // given
        ShoppingListItem listItem = shoppingList.add(itemType);
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.deleteShoppingListItem(shoppingList.getId().getId(), listItem.getId().getId());

        // then
        ResponseTestUtil.assertResponseStatus(response, NO_CONTENT);

        shoppingList = shoppingListRepository.get(shoppingList.getId());
        assertTrue(shoppingList.getItems().isEmpty());
    }

    @Test
    public void deleteItem_invalidListId() {
        // when
        Response response = api.deleteShoppingListItem(INVALID_ID, "ff428927-8618-4f3d-b1a8-a38a82c42c38");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void deleteItem_unknownListId() {
        // when
        Response response = api.deleteShoppingListItem("26490c45-3ef2-422b-bf28-de14c04fce61", "ff428927-8618-4f3d-b1a8-a38a82c42c38");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void deleteItem_invalidListItemId() {
        // when
        Response response = api.deleteShoppingListItem("26490c45-3ef2-422b-bf28-de14c04fce61", INVALID_ID);

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void deleteItem_unknownListItemId() {
        // given
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.deleteShoppingListItem(shoppingList.getId().getId(), "ff428927-8618-4f3d-b1a8-a38a82c42c38");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void emptyCart() {
        // given
        shoppingListRepository.add(shoppingList);

        // when
        Response response = api.emptyShoppingListCart(shoppingList.getId().getId());

        // then
        ResponseTestUtil.assertResponseStatus(response, NO_CONTENT);
    }

    @Test
    public void emptyCart_invalidListId() {
        // when
        Response response = api.emptyShoppingListCart(INVALID_ID);

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void emptyCart_unknownListId() {
        // when
        Response response = api.emptyShoppingListCart("26490c45-3ef2-422b-bf28-de14c04fce61");

        // then
        ResponseTestUtil.assertResponseStatus(response, BAD_REQUEST);
    }
}