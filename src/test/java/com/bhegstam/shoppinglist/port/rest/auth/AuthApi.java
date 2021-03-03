package com.bhegstam.shoppinglist.port.rest.auth;

import com.bhegstam.shoppinglist.port.rest.TestClientFactory;

import javax.ws.rs.core.Response;

public class AuthApi {
    private final String apiVersion;
    private final String serviceUrl;

    public AuthApi(String apiVersion, String serviceUrl) {
        this.apiVersion = apiVersion;
        this.serviceUrl = serviceUrl;
    }

    public Response getToken(String username, String password) {
        return TestClientFactory
                .createClient(username, password)
                .target(serviceUrl)
                .path("auth")
                .request()
                .accept(apiVersion)
                .post(null);
    }

    public Response ping(String token) {
        return TestClientFactory
                .createClient(token)
                .target(serviceUrl)
                .path("auth/ping")
                .request()
                .accept(apiVersion)
                .get();
    }
}
