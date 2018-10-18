package com.bhegstam.shoppinglist.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

public class ShoppingListApplicationConfiguration extends Configuration {
    @NotNull
    @JsonProperty
    private String jwtTokenSecret;

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    public byte[] getJwtTokenSecret() {
        try {
            return jwtTokenSecret.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }
}
