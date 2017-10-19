package com.bhegstam.configuration;

class ApplicationConfigurationException extends RuntimeException {
    ApplicationConfigurationException(String message) {
        super(message);
    }

    ApplicationConfigurationException(Throwable cause) {
        super(cause);
    }
}
