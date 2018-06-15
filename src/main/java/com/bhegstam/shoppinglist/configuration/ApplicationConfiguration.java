package com.bhegstam.shoppinglist.configuration;

import com.bhegstam.shoppinglist.configuration.property.Database;
import com.bhegstam.shoppinglist.configuration.property.Server;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationConfiguration {
    private Server server;
    private Database database;

    public Jdbi getJdbi() {
        Jdbi jdbi = Jdbi.create(database.getUrl(), database.getUsername(), database.getPassword());
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }
}
