package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.port.persistence.RepositoryFactory;
import com.bhegstam.shoppinglist.port.rest.JsonMapper;
import com.bhegstam.shoppinglist.port.rest.TokenGenerator;
import com.bhegstam.shoppinglist.util.DropwizardAppRuleFactory;
import com.bhegstam.shoppinglist.util.TestData;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.hamcrest.CoreMatchers;
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.bhegstam.shoppinglist.port.rest.ResponseTestUtil.assertResponseStatus;
import static com.bhegstam.shoppinglist.port.rest.shoppinglist.RestApiMimeType.SHOPPING_LIST_1_0;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ItemTypeIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    private final JsonMapper jsonMapper = new JsonMapper();
    private ItemTypeApi api;
    private ItemTypeRepository itemTypeRepository;
    private ShoppingListRepository shoppingListRepository;

    @Before
    public void setUp() throws JoseException {
        String serviceUrl = "http://localhost:" + service.getLocalPort() + "/api/";
        String token = TokenGenerator.generate(TestData.ADMIN, service.getConfiguration().getJwtTokenSecret());
        api = new ItemTypeApi(SHOPPING_LIST_1_0, serviceUrl, token);

        RepositoryFactory repositoryFactory = new RepositoryFactory(service.getEnvironment(), service.getConfiguration().getDataSourceFactory());
        itemTypeRepository = repositoryFactory.createItemTypeRepository();
        shoppingListRepository = repositoryFactory.createShoppingListRepository();
    }

    @Test
    public void postItemType() {
        // when
        Response response = api.postItemType("{ \"name\": \"Apples\" }");

        // then
        assertResponseStatus(response, CREATED);

        JsonNode responseJson = jsonMapper.read(response);
        String itemTypeId = responseJson.findValue("id").asText();
        assertThat(itemTypeId, notNullValue());

        ItemType itemType = itemTypeRepository.get(ItemTypeId.parse(itemTypeId));
        assertThat(itemType.getName(), is("Apples"));
    }

    @Test
    public void postItemType_emptyName() {
        // when
        Response response = api.postItemType("{ \"name\": \"\" }");

        // then
        assertResponseStatus(response, 422);
    }

    @Test
    public void postItemType_missingName() {
        // when
        Response response = api.postItemType("{ }");

        // then
        assertResponseStatus(response, 422);
    }

    @Test
    public void postItemType_itemTypeWithSameNameAlreadyExists() {
        // given
        ItemType existingItemType = ItemType.create("Apples");
        itemTypeRepository.add(existingItemType);

        // when
        Response response = api.postItemType("{ \"name\": \"Apples\" }");

        // then
        assertResponseStatus(response, CONFLICT);

        JsonNode responseJson = jsonMapper.read(response);
        assertThat(responseJson.get("errorCode").asText(), is("ITEM_TYPE_NAME_ALREADY_TAKEN"));
    }

    @Test
    public void getItemTypes_noItemTypesExist() {
        // when
        Response response = api.getItemTypes();

        // then
        JsonNode responseJson = jsonMapper.read(response);
        ArrayNode itemTypes = (ArrayNode) responseJson.findValue("itemTypes");

        assertThat(itemTypes.size(), CoreMatchers.is(0));
    }

    @Test
    public void getItemTypes() {
        // given
        ItemType itemType = ItemType.create("Apples");
        itemTypeRepository.add(itemType);

        // when
        Response response = api.getItemTypes();

        // then
        JsonNode responseJson = jsonMapper.read(response);
        JsonNode itemTypes = responseJson.findValue("itemTypes");

        assertThat(itemTypes.size(), CoreMatchers.is(1));

        JsonNode itemTypeJson = itemTypes.get(0);
        assertThat(itemTypeJson.findValue("id").asText(), is(itemType.getId().getId()));
        assertThat(itemTypeJson.findValue("name").asText(), is(itemType.getName()));
    }

    @Test
    public void deleteItemType() {
        // given
        ItemType itemType = ItemType.create("Apples");
        itemTypeRepository.add(itemType);

        // when
        Response response = api.deleteItemType(itemType.getId().getId());

        // then
        assertResponseStatus(response, NO_CONTENT);

        assertTrue(itemTypeRepository.getItemTypes().isEmpty());
    }

    @Test
    public void deleteItemType_unknownId() {
        // when
        Response response = api.deleteItemType("a4a255b5-cdbd-47ac-8868-69a7475adc2a");

        // then
        assertResponseStatus(response, NO_CONTENT);
    }

    @Test
    public void deleteItemType_invalidId() {
        // when
        Response response = api.deleteItemType("invalid-id");

        // then
        assertResponseStatus(response, BAD_REQUEST);
    }

    @Test
    public void deleteItemType_usedByShoppingList() {
        // given
        ItemType itemType = ItemType.create("Apples");
        itemTypeRepository.add(itemType);

        ShoppingList list = ShoppingList.create("foo");
        list.add(itemType);
        shoppingListRepository.persist(list);

        // when
        Response response = api.deleteItemType(itemType.getId().getId());

        // then
        assertResponseStatus(response, CONFLICT);

        JsonNode responseJson = jsonMapper.read(response);
        assertThat(responseJson.get("errorCode").asText(), is("ITEM_TYPE_USED_IN_SHOPPING_LIST"));
        assertThat(responseJson.get("message").asText(), is(itemType.getId().getId()));
    }
}
