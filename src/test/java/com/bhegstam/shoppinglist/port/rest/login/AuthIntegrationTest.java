package com.bhegstam.shoppinglist.port.rest.login;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.port.rest.TestClientFactory;
import com.bhegstam.shoppinglist.util.DropwizardAppRuleFactory;
import com.bhegstam.shoppinglist.util.TestData;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AuthIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup(service.getConfiguration());
    private String serviceUrl;

    @Before
    public void setUp() {
        serviceUrl = "http://localhost:" + service.getLocalPort() + "/api/auth/";
    }

    @Test
    public void getTokenForExistingAdmin() throws IOException {
        // Generate new token
        Response getTokenResponse = TestClientFactory
                .createClient(TestData.ADMIN.getUsername(), TestData.ADMIN_PASSWORD)
                .target(serviceUrl)
                .path("token")
                .request()
                .get();

        assertThat(getTokenResponse.getStatus(), is(OK.getStatusCode()));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(getTokenResponse.readEntity(String.class));
        String token = responseJson.findValue("token").asText();
        assertThat(token, notNullValue());

        // Test it against authenticated ping
        Response pingResponse = TestClientFactory
                .createClient(token)
                .target(serviceUrl)
                .path("ping")
                .request()
                .get();
        assertThat(pingResponse.getStatus(), is(OK.getStatusCode()));
    }
}
