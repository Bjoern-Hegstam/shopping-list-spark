package com.bhe.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.net.URL;

class ApplicationConfigurationLoader {
    static ApplicationConfiguration load(URL url) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(url, ApplicationConfiguration.class);
        } catch (IOException e) {
            throw new ApplicationConfigurationException(e);
        }
    }
}
