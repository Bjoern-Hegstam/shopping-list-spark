package com.bhe.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ApplicationConfigurationLoader {
    public static ApplicationConfiguration load(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(new File(path), ApplicationConfiguration.class);
        } catch (IOException e) {
            throw new ApplicationConfigurationException(e);
        }
    }
}
