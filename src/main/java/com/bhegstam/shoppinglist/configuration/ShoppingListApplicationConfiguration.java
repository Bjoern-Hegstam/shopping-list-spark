package com.bhegstam.shoppinglist.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

public class ShoppingListApplicationConfiguration extends Configuration {
    @NotNull
    @JsonProperty
    private String jwtTokenSecret;

    @Valid
    @NotNull
    @JsonProperty
    private final DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public byte[] getJwtTokenSecret() {
        return jwtTokenSecret.getBytes(StandardCharsets.UTF_8);
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }
}
