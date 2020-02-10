package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.shoppinglist.domain.UserRepository;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class RepositoryFactory {
    private final Jdbi jdbi;

    public RepositoryFactory(Environment environment, DataSourceFactory dataSourceFactory) {
        jdbi = new JdbiFactory().build(environment, dataSourceFactory, "datasource");
    }

    public UserRepository createUserRepository() {
        return jdbi.onDemand(JdbiUserRepository.class);
    }

    public ShoppingListRepository createShoppingListRepository() {
        return jdbi.onDemand(JdbiShoppingListRepository.class);
    }
}
