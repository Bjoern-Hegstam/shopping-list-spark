package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.port.rest.JsonMapper;
import com.bhegstam.shoppinglist.port.rest.TokenGenerator;
import com.bhegstam.shoppinglist.util.DropwizardAppRuleFactory;
import com.bhegstam.shoppinglist.util.TestData;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import com.fasterxml.jackson.databind.JsonNode;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static com.bhegstam.shoppinglist.port.rest.ResponseTestUtil.assertResponseStatus;
import static com.bhegstam.shoppinglist.port.rest.user.RestApiMimeType.USER_1_0;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    private final String serviceUrl = "http://localhost:" + service.getLocalPort() + "/api/";

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    private UserApi api;

    @Before
    public void setUp() throws JoseException {
        String token = TokenGenerator.generate(TestData.ADMIN.getUsername(), service.getConfiguration().getJwtTokenSecret());
        api = new UserApi(USER_1_0, serviceUrl, token);
    }

    @Test
    public void createUser() throws JoseException {
        // create user
        Response createUserResponse = api.createUser("{\"username\": \"test-username\", \"password\": \"test-password\", \"email\": \"test-email\"}");

        assertResponseStatus(createUserResponse, CREATED);
        String userId = JsonMapper.read(createUserResponse).get("id").asText();

        // verify new user
        Response updateUserResponse = api.updateUser(userId, "{\"verified\":true, \"role\":\"USER\"}");
        assertResponseStatus(updateUserResponse, OK);

        // as new user get info about self
        // TODO: Replace with use of the auth resource
        String userToken = TokenGenerator.generate("test-username", service.getConfiguration().getJwtTokenSecret());
        UserApi userApi = new UserApi(USER_1_0, serviceUrl, userToken);

        Response getUsersResponse = userApi.getAuthenticatedUser();
        assertResponseStatus(getUsersResponse, OK);

        JsonNode usersJson = JsonMapper.read(getUsersResponse);
        assertThat(usersJson.get("id").asText(), is(userId));
        assertThat(usersJson.get("username").asText(), is("test-username"));
        assertThat(usersJson.get("verified").asBoolean(), is(true));
        assertThat(usersJson.get("role").asText(), is("USER"));

        JsonNode workspacesJson = usersJson.get("workspaces");
        assertThat(workspacesJson.size(), is(1)); // TODO: Implement. A default workspace should have been created for the user.
    }

    // TODO: Migrate tests in UserShoppingListApplicationIntegrationTest to this class
}
