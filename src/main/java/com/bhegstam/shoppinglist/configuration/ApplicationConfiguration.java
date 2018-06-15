package com.bhegstam.shoppinglist.configuration;

import com.bhegstam.shoppinglist.configuration.property.Database;
import com.bhegstam.shoppinglist.configuration.property.Server;
import lombok.Data;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@Data
public class ApplicationConfiguration {
    private final Server server;
    private final Database database;

    public ApplicationConfiguration() {
        server = new Server();
        database = new Database();
    }

    public Jdbi getJdbi() {
        Jdbi jdbi = Jdbi.create(database.getUrl(), database.getUsername(), database.getPassword());
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }
}
