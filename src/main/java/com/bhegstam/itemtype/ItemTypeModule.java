package com.bhegstam.itemtype;

import com.bhegstam.itemtype.db.JdbcItemTypeRepository;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import com.google.inject.AbstractModule;

public class ItemTypeModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ItemTypeRepository.class).to(JdbcItemTypeRepository.class);
    }
}
