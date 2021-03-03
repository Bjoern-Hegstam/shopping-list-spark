package com.bhegstam.shoppinglist.port.rest.admin;

import com.bhegstam.shoppinglist.port.rest.TestClientFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class UserAdminApi {
    private final String apiVersion;
    private final WebTarget webTarget;

    public UserAdminApi(String apiVersion, String serviceUrl, String token) {
        this.apiVersion = apiVersion;
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }

    public Response createUser(String json) {
        return webTarget
                .path("admin")
                .path("user")
                .request()
                .accept(apiVersion)
                .post(Entity.json(json));
    }

    public Response updateUser(String userId, String json) {
        return webTarget
                .path("admin")
                .path("user")
                .path(userId)
                .request()
                .accept(apiVersion)
                .put(Entity.json(json));
    }

    public Response getUser(String userId) {
        return webTarget
                .path("admin")
                .path("user")
                .path(userId)
                .request()
                .accept(apiVersion)
                .get();
    }
}
