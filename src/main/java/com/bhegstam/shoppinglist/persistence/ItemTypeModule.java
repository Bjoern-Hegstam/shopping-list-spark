package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import com.google.inject.AbstractModule;

public class ItemTypeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ItemTypeRepository.class).to(JdbcItemTypeRepository.class);
    }
}
