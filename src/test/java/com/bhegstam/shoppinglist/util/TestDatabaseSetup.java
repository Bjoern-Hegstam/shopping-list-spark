package com.bhegstam.shoppinglist.util;

import com.bhegstam.shoppinglist.configuration.DbMigrationBundle;
import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.port.persistence.RepositoryFactory;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.configuration.FileConfigurationSourceProvider;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.validation.BaseValidator;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseSetup implements TestRule {
    private static final String TEST_CONFIG_FILENAME = "test-config.yml";

    private final RepositoryFactory repositoryFactory;
    private final ManagedDataSource dataSource;

    public TestDatabaseSetup() {
        ShoppingListApplicationConfiguration config = loadConfiguration();
        Environment environment = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);

        new DbMigrationBundle().run(config, environment);

        DataSourceFactory dataSourceFactory = config.getDataSourceFactory();
        repositoryFactory = new RepositoryFactory(environment, dataSourceFactory);
        dataSource = dataSourceFactory.build(new MetricRegistry(), "cleanup");
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

    private void after() {
        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement("delete from application_user").execute();
            conn.prepareStatement("delete from shopping_list_item").execute();
            conn.prepareStatement("delete from shopping_list").execute();
            conn.prepareStatement("delete from item_type").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RepositoryFactory getRepositoryFactory() {
        return repositoryFactory;
    }

    private ShoppingListApplicationConfiguration loadConfiguration() {
        try {
            return new YamlConfigurationFactory<>(
                    ShoppingListApplicationConfiguration.class,
                    BaseValidator.newValidator(),
                    Jackson.newObjectMapper(new YAMLFactory()),
                    ""
            ).build(new FileConfigurationSourceProvider(), ResourceHelpers.resourceFilePath(TEST_CONFIG_FILENAME));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
