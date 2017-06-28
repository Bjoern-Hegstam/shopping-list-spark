package com.bhe.configuration;

import com.google.inject.AbstractModule;

import java.net.URL;

public class ConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ApplicationConfiguration.class)
                .toProvider(() -> {
                    String configurationName = "conf/application.yml";
                    URL confUrl = getClass()
                            .getClassLoader()
                            .getResource(configurationName);

                    if (confUrl == null) {
                        throw new ApplicationConfigurationException("Configuration not found: " + configurationName);
                    }

                    ApplicationConfiguration configuration = ApplicationConfigurationLoader.load(confUrl.getFile());
                    new EnvironmentVariableReplacer().applyTo(configuration);
                    return configuration;
                })
                .asEagerSingleton();
    }
}
