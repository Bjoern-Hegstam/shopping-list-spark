package com.bhegstam.util;

import com.bhegstam.shoppinglist.configuration.ApplicationConfiguration;
import com.bhegstam.shoppinglist.configuration.EnvironmentVariable;
import com.bhegstam.shoppinglist.persistence.DatabaseMigrator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.jdbi.v3.core.Jdbi;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.util.Optional;

public class TestDatabaseSetup implements TestRule {

    private ApplicationConfiguration conf;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    before();
                    base.evaluate();
                } finally {
                    after();
                }
            }
        };
    }

    private void before() {
        if (conf == null) {
            conf = loadConfiguration();
            new DatabaseMigrator(conf.getDatabase()).migrateDatabase();
        }
    }

    private void after() {
        conf.getJdbi().useHandle(handle -> {
            handle.createUpdate("delete from application_user").execute();
            handle.createUpdate("delete from item_type").execute();
        });
    }

    public Jdbi getJdbi() {
        return conf.getJdbi();
    }

    private ApplicationConfiguration loadConfiguration() {
        String filename = Optional
                .ofNullable(System.getenv(EnvironmentVariable.CONF_FILENAME))
                .orElse(EnvironmentVariable.DEFAULT_CONF_FILENAME);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(
                    this.getClass().getClassLoader().getResource(filename),
                    ApplicationConfiguration.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
