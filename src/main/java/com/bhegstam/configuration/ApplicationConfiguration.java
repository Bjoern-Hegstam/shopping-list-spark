package com.bhegstam.configuration;

import com.bhegstam.configuration.property.Database;
import com.bhegstam.configuration.property.Server;
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
