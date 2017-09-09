package com.bhe.configuration;

import com.bhe.configuration.property.Database;
import com.bhe.configuration.property.Server;
import lombok.Data;

@Data
public class ApplicationConfiguration {
    private Server server;
    private Database database;
}
