package com.bhegstam.shoppinglist.port.rest.login;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.port.rest.AddBasicAuthHeaderFilter;
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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class LoginIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup(service.getConfiguration());
    private String serviceUrl;

    @Before
    public void setUp() {
        serviceUrl = "http://localhost:" + service.getLocalPort() + "/application/api/";
    }

    @Test
    public void login_existingUser() throws IOException {
        Response response = createClient(TestData.ADMIN.getUsername(), TestData.ADMIN_PASSWORD)
                .target(serviceUrl)
                .path("login")
                .request()
                .get();

        assertThat(response.getStatus(), is(OK.getStatusCode()));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.readEntity(String.class));
        assertThat(responseJson.findValue("token").asText(), notNullValue());
    }

    private Client createClient(String username, String password) {
        Client client = ClientBuilder.newClient();
        client.register(new AddBasicAuthHeaderFilter(username, password));
        return client;
    }
}
