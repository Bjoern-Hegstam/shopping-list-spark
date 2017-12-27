package com.bhegstam.shoppinglist;

import com.bhegstam.shoppinglist.db.JdbcShoppingListRepository;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.google.inject.AbstractModule;

public class ShoppingListModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ShoppingListRepository.class).to(JdbcShoppingListRepository.class);
    }
}
