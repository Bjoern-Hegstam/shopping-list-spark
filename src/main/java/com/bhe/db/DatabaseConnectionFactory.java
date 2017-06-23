package com.bhe.db;

import com.bhe.configuration.ApplicationConfiguration;
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
        this(
                conf.getDatabase().getUrl(),
                conf.getDatabase().getUser(),
                conf.getDatabase().getPassword()
        );
    }

    private DatabaseConnectionFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void withConnection(Consumer<Connection> consumer) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            consumer.accept(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
