package com.bhegstam.shoppinglist.configuration;

import com.bhegstam.shoppinglist.configuration.property.Database;
import com.bhegstam.shoppinglist.configuration.property.Server;
import lombok.Data;

@Data
public class ApplicationConfiguration {
    private Server server;
    private Database database;

    public ApplicationConfiguration() {
        server = new Server();
        database = new Database();
    }
}
