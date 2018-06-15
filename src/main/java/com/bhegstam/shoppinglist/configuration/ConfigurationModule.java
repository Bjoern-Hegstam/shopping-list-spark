package com.bhegstam.shoppinglist.configuration;

import com.google.inject.AbstractModule;

public class ConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ApplicationConfiguration.class)
                .toProvider(ApplicationConfiguration::new)
                .asEagerSingleton();
    }
}
