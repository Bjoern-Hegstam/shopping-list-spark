package com.bhegstam.shoppinglist.configuration;

import com.bhegstam.shoppinglist.configuration.property.Database;
import com.bhegstam.shoppinglist.configuration.property.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ShoppingListShoppingListApplicationConfigurationTest {
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void loadConfiguration() {
        // given
        String conf = "" +
                "server:\n" +
                "  port: 4567\n" +
                "\n" +
                "database:\n" +
                "  url: TEST_URL\n" +
                "  username: TEST_USER\n" +
                "  password: TEST_PASSWORD\n";

        // when
        ShoppingListApplicationConfiguration value = load(conf);

        // then
        assertNotNull(value);

        Server server = value.getServer();
        assertNotNull(server);
        errorCollector.checkThat(server.getPort(), is(4567));

        Database database = value.getDataSourceFactory();
        assertNotNull(database);
        errorCollector.checkThat(database.getUrl(), is("TEST_URL"));
        errorCollector.checkThat(database.getUsername(), is("TEST_USER"));
        errorCollector.checkThat(database.getPassword(), is("TEST_PASSWORD"));
    }

    private ShoppingListApplicationConfiguration load(String conf) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(conf, ShoppingListApplicationConfiguration.class);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return null;
    }
}