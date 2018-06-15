package com.bhegstam.shoppinglist.configuration;

import org.flywaydb.core.Flyway;

public class Migrations {
    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(
                System.getenv(EnvironmentVariable.JDBC_DATABASE_URL),
                System.getenv(EnvironmentVariable.JDBC_DATABASE_USERNAME),
                System.getenv(EnvironmentVariable.JDBC_DATABASE_PASSWORD)
        );
        flyway.migrate();
    }
}