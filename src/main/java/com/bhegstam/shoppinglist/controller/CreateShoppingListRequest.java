package com.bhegstam.shoppinglist.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CreateShoppingListRequest {
    private String name;

    public CreateShoppingListRequest(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    static CreateShoppingListRequest fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, CreateShoppingListRequest.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
