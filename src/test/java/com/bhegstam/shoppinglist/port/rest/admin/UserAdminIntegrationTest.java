package com.bhegstam.shoppinglist.port.rest.admin;

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
import org.junit.rules.ErrorCollector;

import javax.ws.rs.core.Response;

import static com.bhegstam.shoppinglist.port.rest.ResponseTestUtil.assertResponseStatus;
import static com.bhegstam.shoppinglist.port.rest.admin.RestApiMimeType.USER_ADMIN_1_0;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.is;

public class UserAdminIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    private final String serviceUrl = "http://localhost:" + service.getLocalPort() + "/api/";

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private UserAdminApi userAdminApi;

    @Before
    public void setUp() throws JoseException {
        String token = TokenGenerator.generate(TestData.ADMIN.getUsername(), service.getConfiguration().getJwtTokenSecret());
        userAdminApi = new UserAdminApi(USER_ADMIN_1_0, serviceUrl, token);
    }

    @Test
    public void updateUser_setVerified() {
        // when
        Response updateUserResponse = userAdminApi.updateUser(
                TestData.UNVERIFIED_USER.getId().getId(),
                "{ \"role\":\"USER\", \"verified\":true }"
        );
        assertResponseStatus(updateUserResponse, OK);

        // then
        Response getUserResponse = userAdminApi.getUser(TestData.UNVERIFIED_USER.getId().getId());
        assertResponseStatus(updateUserResponse, OK);

        JsonNode userResponseJson = JsonMapper.read(getUserResponse);
        errorCollector.checkThat(userResponseJson.get("role").asText(), is("USER"));
        errorCollector.checkThat(userResponseJson.get("verified").asBoolean(), is(true));
    }

    @Test
    public void updateUser_makeAdmin() {
        // when
        Response updateUserResponse = userAdminApi.updateUser(
                TestData.USER.getId().getId(),
                "{ \"role\":\"ADMIN\", \"verified\":true }"
        );

        // then
        Response getUserResponse = userAdminApi.getUser(TestData.USER.getId().getId());
        JsonNode userResponseJson = JsonMapper.read(getUserResponse);

        errorCollector.checkThat(userResponseJson.get("role").asText(), is("ADMIN"));
        errorCollector.checkThat(userResponseJson.get("verified").asBoolean(), is(true));
    }
}
