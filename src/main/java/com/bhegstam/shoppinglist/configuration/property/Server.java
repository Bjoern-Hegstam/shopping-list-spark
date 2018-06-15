package com.bhegstam.shoppinglist.configuration.property;

import lombok.Data;

import static com.bhegstam.shoppinglist.configuration.EnvironmentVariable.PORT;

@Data
public class Server {
    public static final int DEFAULT_PORT = 4567;

    private int port;

    public Server() {
        this.port = System.getenv().containsKey(PORT) ? Integer.parseInt(System.getenv(PORT)) : DEFAULT_PORT;
    }
}
