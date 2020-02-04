package com.bhegstam.shoppinglist.util;

import com.bhegstam.shoppinglist.configuration.DbMigrationBundle;
import com.bhegstam.shoppinglist.configuration.ShoppingListApplicationConfiguration;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.shoppinglist.port.persistence.RepositoryFactory;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
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

import javax.validation.ValidatorFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;

public class TestDatabaseSetup implements TestRule {
    private static final List<String> CLEANUP_SQL_STATEMENTS = asList(
            "delete from application_user",
            "delete from shopping_list_item",
            "delete from shopping_list",
            "delete from item_type"
    );

    private final RepositoryFactory repositoryFactory;
    private final ManagedDataSource dataSource;

    public TestDatabaseSetup() {
        this(null);
    }

    public TestDatabaseSetup(ShoppingListApplicationConfiguration config) {
        if (config == null) {
            config = loadConfiguration();
        }

        new DbMigrationBundle().run(config, null);

        ValidatorFactory validatorFactory = mock(ValidatorFactory.class);
        HealthCheckRegistry healthCheckRegistry = mock(HealthCheckRegistry.class);
        Environment environment = new Environment("test-env", Jackson.newObjectMapper(), validatorFactory, new MetricRegistry(), null, healthCheckRegistry, config);

        DataSourceFactory dataSourceFactory = config.getDataSourceFactory();

        repositoryFactory = new RepositoryFactory(environment, dataSourceFactory);
        dataSource = dataSourceFactory.build(new MetricRegistry(), "cleanup");
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

    private void before() {
        emptyDb();
        insertUsers();
    }

    private void emptyDb() {
        try (Connection conn = dataSource.getConnection()) {
            for (String statement : CLEANUP_SQL_STATEMENTS) {
                conn.prepareStatement(statement).execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertUsers() {
        UserRepository userRepository = repositoryFactory.createUserRepository();
        userRepository.add(TestData.ADMIN);
        userRepository.add(TestData.USER);
        userRepository.add(TestData.UNVERIFIED_USER);
    }

    private void after() {
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
            ).build(new FileConfigurationSourceProvider(), ResourceHelpers.resourceFilePath(TestData.getTestConfigFilename()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
