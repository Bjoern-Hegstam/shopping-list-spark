package com.bhegstam.shoppinglist.configuration;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.flywaydb.core.Flyway;

public class DbMigrationBundle implements ConfiguredBundle<ShoppingListApplicationConfiguration> {
    @Override
    public void run(ShoppingListApplicationConfiguration config, Environment environment) {
        DataSourceFactory dataSourceFactory = config.getDataSourceFactory();
        Flyway.configure()
                .dataSource(dataSourceFactory.getUrl(), dataSourceFactory.getUser(), dataSourceFactory.getPassword())
                .load()
                .migrate();
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }
}
