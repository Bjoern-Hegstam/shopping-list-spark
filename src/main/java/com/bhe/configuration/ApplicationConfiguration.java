package com.bhe.configuration;

import com.bhe.configuration.property.Database;
import lombok.Data;

@Data
public class ApplicationConfiguration {
    private Database database;
}
