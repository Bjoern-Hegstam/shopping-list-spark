package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.port.rest.TestClientFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class UserApi {
    private final String apiVersion;
    private final WebTarget webTarget;

    UserApi(String apiVersion, String serviceUrl, String token) {
        this.apiVersion = apiVersion;
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }

    public Response createUser(String json) {
        return webTarget
                .path("user")
                .request()
                .accept(apiVersion)
                .post(Entity.json(json));
    }

    public Response getAuthenticatedUser() {
        return webTarget
                .path("user")
                .request()
                .accept(apiVersion)
                .get();
    }

    public Response updateUser(String userId, String json) {
        return webTarget
                .path("user")
                .path(userId)
                .request()
                .accept(apiVersion)
                .put(Entity.json(json));
    }
}
