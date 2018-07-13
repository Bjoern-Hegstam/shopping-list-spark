package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.port.rest.auth.AddBearerTokenHeaderFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class TestClientFactory {
    public static Client createClient(String token) {
        Client client = ClientBuilder.newClient();
        client.register(new AddBearerTokenHeaderFilter(token));
        return client;
    }

    public static Client createClient(String username, String password) {
        Client client = ClientBuilder.newClient();
        client.register(new AddBasicAuthHeaderFilter(username, password));
        return client;
    }
}
