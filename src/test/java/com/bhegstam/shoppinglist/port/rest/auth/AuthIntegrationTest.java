package com.bhegstam.shoppinglist.port.rest.auth;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.port.rest.JsonMapper;
import com.bhegstam.shoppinglist.util.DropwizardAppRuleFactory;
import com.bhegstam.shoppinglist.util.TestData;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.bhegstam.shoppinglist.port.rest.auth.RestApiMimeType.AUTH_1_0;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AuthIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup(service.getConfiguration());
    private AuthApi authApi;

    @Before
    public void setUp() {
        authApi = new AuthApi(AUTH_1_0, "http://localhost:" + service.getLocalPort() + "/api/");
    }

    @Test
    public void getTokenForExistingAdmin() throws IOException {
        // Generate new token
        Response getTokenResponse = authApi.getToken(TestData.ADMIN.getUsername(), TestData.ADMIN_PASSWORD);
        assertThat(getTokenResponse.getStatus(), is(OK.getStatusCode()));

        String token = JsonMapper.read(getTokenResponse).get("token").asText();
        assertThat(token, notNullValue());

        // Test it against authenticated ping endpoint
        Response pingResponse = authApi.ping(token);
        assertThat(pingResponse.getStatus(), is(OK.getStatusCode()));
    }

    // TODO: Add test verifying that an unverified user cannot get a token
}
