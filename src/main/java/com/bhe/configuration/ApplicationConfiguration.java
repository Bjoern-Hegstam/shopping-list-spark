package com.bhe.configuration;

import com.bhe.configuration.property.Database;

public class ApplicationConfiguration {
    private Database database;

    public ApplicationConfiguration() {
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
