package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.util.DropwizardAppRuleFactory;
import com.bhegstam.shoppinglist.util.TestData;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.jose4j.lang.JoseException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;

public class ItemTypeIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ShoppingListApplicationConfiguration> service = DropwizardAppRuleFactory.forIntegrationTest();

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    private final JsonMapper jsonMapper = new JsonMapper();

    @Before
    public void setUp() throws JoseException {
        String serviceUrl = "http://localhost:" + service.getLocalPort() + "/api/";
        String token = TokenGenerator.generate(TestData.ADMIN, service.getConfiguration().getJwtTokenSecret());
        new ItemTypeApi(token, serviceUrl);
    }

    // TODO: Fill me in
}
