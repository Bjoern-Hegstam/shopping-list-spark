package com.bhe.configuration;

class ApplicationConfigurationException extends RuntimeException {
    ApplicationConfigurationException(String message) {
        super(message);
    }

    ApplicationConfigurationException(Throwable cause) {
        super(cause);
    }
}
