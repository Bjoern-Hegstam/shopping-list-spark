package com.bhegstam.shoppinglist.util;

import com.bhegstam.shoppinglist.ShoppingListApplication;
import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public class DropwizardAppRuleFactory {
    public static DropwizardAppRule<ShoppingListApplicationConfiguration> forIntegrationTest() {
        return new DropwizardAppRule<>(
                ShoppingListApplication.class,
                ResourceHelpers.resourceFilePath(TestData.TEST_CONFIG_FILENAME)
        );
    }
}
