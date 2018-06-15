package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.configuration.property.Database;
import org.flywaydb.core.Flyway;

public class DatabaseMigrator {
    private final Database database;

    public DatabaseMigrator(Database database) {
        this.database = database;
    }

    public void migrateDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(
                database.getUrl(),
                database.getUsername(),
                database.getPassword()
        );
        flyway.migrate();
    }
}
