package com.bhe.db;

import com.bhe.configuration.ApplicationConfiguration;
import com.bhe.configuration.property.Database;
import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class DatabaseConnectionFactory {
    private final String url;
    private final String username;
    private final String password;

    @Inject
    public DatabaseConnectionFactory(ApplicationConfiguration conf) {
        Database database = conf.getDatabase();

        this.url = database.getUrl();
        this.username = database.getUser();
        this.password = database.getPassword();
    }

    public void withConnection(Consumer<Connection> consumer) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            consumer.accept(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
