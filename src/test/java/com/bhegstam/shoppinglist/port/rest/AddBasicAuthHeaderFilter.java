package com.bhegstam.shoppinglist.port.rest;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.UnsupportedEncodingException;

public class AddBasicAuthHeaderFilter implements ClientRequestFilter {
    private final String encodedToken;

    AddBasicAuthHeaderFilter(String username, String password) {
        String token = username + ":" + password;
        try {
            encodedToken = Base64.encodeAsString(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext
                .getHeaders()
                .add(HttpHeaders.AUTHORIZATION, "Basic " + encodedToken);

    }
}
