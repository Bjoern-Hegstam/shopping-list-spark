package com.bhegstam.shoppinglist.port.rest.login;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

public class AddBearerTokenHeaderFilter implements ClientRequestFilter {
    private final String token;

    public AddBearerTokenHeaderFilter(String token) {
        this.token = token;
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext
                .getHeaders()
                .add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
