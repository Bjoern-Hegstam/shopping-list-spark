package com.bhegstam.util;

import com.bhegstam.shoppinglist.configuration.property.Database;
import com.bhegstam.shoppinglist.persistence.DatabaseMigrator;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TestDatabaseSetup implements TestRule {

    private Jdbi jdbi;

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
        Database database = new Database(
                "jdbc:h2:mem:testing;DB_CLOSE_DELAY=-1",
                null,
                null
        );

        jdbi = Jdbi.create(database.getUrl());
        jdbi.installPlugin(new SqlObjectPlugin());

        new DatabaseMigrator(database).migrateDatabase();
    }

    private void after() {
        jdbi.useHandle(handle -> handle.createUpdate("delete from application_user").execute());
    }

    public Jdbi getJdbi() {
        return jdbi;
    }
}
