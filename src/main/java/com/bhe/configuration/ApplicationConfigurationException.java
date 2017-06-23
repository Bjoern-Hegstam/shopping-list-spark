package com.bhe.configuration;

public class ApplicationConfigurationException extends RuntimeException {
    public ApplicationConfigurationException(String message) {
        super(message);
    }

    public ApplicationConfigurationException(Throwable cause) {
        super(cause);
    }
}
