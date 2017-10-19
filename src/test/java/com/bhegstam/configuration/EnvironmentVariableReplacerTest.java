package com.bhegstam.configuration;

import com.bhegstam.configuration.property.Database;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

public class EnvironmentVariableReplacerTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void replaceDatabaseConfiguration() {
        // given
        Map<String, String> environment = new HashMap<>();
        environment.put("DB_URL", "fooUrl");
        environment.put("DB_USER", "fooUser");
        environment.put("DB_PASSWORD", "fooPassword");


        Database database = new Database();
        database.setUrl("${DB_URL}");
        database.setUser("${DB_USER}");
        database.setPassword("${DB_PASSWORD}");

        ApplicationConfiguration configuration = new ApplicationConfiguration();
        configuration.setDatabase(database);

        // when
        new EnvironmentVariableReplacer(environment).applyTo(configuration);

        // then
        errorCollector.checkThat(database.getUrl(), is("fooUrl"));
        errorCollector.checkThat(database.getUser(), is("fooUser"));
        errorCollector.checkThat(database.getPassword(), is("fooPassword"));
    }
}