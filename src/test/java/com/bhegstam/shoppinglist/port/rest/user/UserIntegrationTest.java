package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.port.rest.JsonMapper;
import com.bhegstam.shoppinglist.port.rest.TokenGenerator;
import com.bhegstam.shoppinglist.port.rest.admin.UserAdminApi;
import com.bhegstam.shoppinglist.port.rest.auth.AuthApi;
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
import static com.bhegstam.shoppinglist.port.rest.admin.RestApiMimeType.USER_ADMIN_1_0;
import static com.bhegstam.shoppinglist.port.rest.auth.RestApiMimeType.AUTH_1_0;
import static com.bhegstam.shoppinglist.port.rest.user.RestApiMimeType.USER_1_0;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    private final String serviceUrl = "http://localhost:" + service.getLocalPort() + "/api/";

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    private AuthApi authApi;
    private UserAdminApi userAdminApi;

    @Before
    public void setUp() throws JoseException {
        authApi = new AuthApi(AUTH_1_0, serviceUrl);

        String token = TokenGenerator.generate(TestData.ADMIN.getUsername(), service.getConfiguration().getJwtTokenSecret());
        userAdminApi = new UserAdminApi(USER_ADMIN_1_0, serviceUrl, token);
    }

    @Test
    public void createAndVerifyUser() {
        // when
        UserId userId = createUser("test-username", "test-password", "test-email");
        verifyUser(userId);

        // then
        UserApi userApi = getUserApi("test-username", "test-password");

        Response getUsersResponse = userApi.getAuthenticatedUser();
        assertResponseStatus(getUsersResponse, OK);

        JsonNode usersJson = JsonMapper.read(getUsersResponse);
        assertThat(usersJson.get("id").asText(), is(userId.getId()));
        assertThat(usersJson.get("username").asText(), is("test-username"));
        assertThat(usersJson.get("verified").asBoolean(), is(true));
        assertThat(usersJson.get("role").asText(), is("USER"));
    }

    private UserId createUser(String username, String password, String email) {
        Response createUserResponse = userAdminApi.createUser(createUserRequestJson(username, password, email));
        assertResponseStatus(createUserResponse, CREATED);
        return UserId.from(JsonMapper.read(createUserResponse).get("id").asText());
    }

    private String createUserRequestJson(final String username, final String password, final String email) {
        return "{\"username\": \"" + username + "\", \"password\": \"" + password + "\", \"email\": \"" + email + "\"}";
    }

    private void verifyUser(UserId userId) {
        Response updateUserResponse = userAdminApi.updateUser(userId.getId(), "{\"verified\":true, \"role\":\"USER\"}");
        assertResponseStatus(updateUserResponse, OK);
    }

    private UserApi getUserApi(String username, String password) {
        Response getTokenResponse = authApi.getToken(username, password);
        assertResponseStatus(getTokenResponse, OK);
        String userToken = JsonMapper.read(getTokenResponse).get("token").asText();
        return new UserApi(USER_1_0, serviceUrl, userToken);
    }

    @Test
    public void createUser_usernameEmpty() {
        // when
        Response createUserResponse = userAdminApi.createUser(createUserRequestJson("", "test-password", "test-email"));

        // then
        assertResponseStatus(createUserResponse, 422);
    }

    @Test
    public void createUser_usernameNotAvailable() {
        // when
        Response createUserResponse = userAdminApi.createUser(createUserRequestJson(TestData.USER.getUsername(), "test-password", "test-email"));

        // then
        assertResponseStatus(createUserResponse, 422);
        assertThat(JsonMapper.read(createUserResponse).get("errorCode").asText(), is("INVALID_USERNAME"));
    }

    @Test
    public void createUser_emailEmpty() {
        // when
        Response createUserResponse = userAdminApi.createUser(createUserRequestJson("test-username", "test-password", ""));

        // then
        assertResponseStatus(createUserResponse, 422);
    }

    @Test
    public void createUser_emailAlreadyInUse() {
        // when
        Response createUserResponse = userAdminApi.createUser(createUserRequestJson("test-username", "test-password", TestData.USER.getEmail()));

        // then
        assertResponseStatus(createUserResponse, 422);
        assertThat(JsonMapper.read(createUserResponse).get("errorCode").asText(), is("INVALID_EMAIL"));
    }

    @Test
    public void getWorkspaces_forNewUser() {
        // given
        UserId userId = createUser("test-username", "test-password", "test-email");
        verifyUser(userId);
        UserApi userApi = getUserApi("test-username", "test-password");

        // when
        Response getWorkspacesResponse = userApi.getWorkspaces();

        // then
        assertResponseStatus(getWorkspacesResponse, OK);
        JsonNode getWorkspacesResponseJson = JsonMapper.read(getWorkspacesResponse);
        JsonNode workspacesJson = getWorkspacesResponseJson.get("workspaces");
        assertThat(workspacesJson.size(), is(1));
        assertThat(workspacesJson.get(0).get("name").asText(), is("Default"));
    }
}
