package com.bhe.configuration;

import com.bhe.configuration.property.Database;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EnvironmentVariableReplacer {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([A-Z_]+)}");
    private final Map<String, String> environment;

    EnvironmentVariableReplacer() {
        this(System.getenv());
    }

    EnvironmentVariableReplacer(Map<String, String> environment) {
        this.environment = environment;
    }

    void applyTo(ApplicationConfiguration configuration) {
        Database database = configuration.getDatabase();
        replaceIfNeeded(database.getUrl(), database::setUrl);
        replaceIfNeeded(database.getUser(), database::setUser);
        replaceIfNeeded(database.getPassword(), database::setPassword);
    }

    private void replaceIfNeeded(String value, Consumer<String> setPropertyValue) {
        extractEnvironmentVariable(value)
                .ifPresent(setPropertyValue);
    }

    private Optional<String> extractEnvironmentVariable(String value) {
        Objects.requireNonNull(value);

        Matcher matcher = VARIABLE_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        return Optional
                .of(matcher.group(1))
                .filter(environment::containsKey)
                .map(environment::get);
    }
}
