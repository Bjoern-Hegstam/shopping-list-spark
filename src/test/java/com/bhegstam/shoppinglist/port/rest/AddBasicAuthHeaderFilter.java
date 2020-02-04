package com.bhegstam.shoppinglist.port.rest;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AddBasicAuthHeaderFilter implements ClientRequestFilter {
    private final String encodedToken;

    AddBasicAuthHeaderFilter(String username, String password) {
        String token = username + ":" + password;
        encodedToken = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext
                .getHeaders()
                .add(HttpHeaders.AUTHORIZATION, "Basic " + encodedToken);

    }
}
