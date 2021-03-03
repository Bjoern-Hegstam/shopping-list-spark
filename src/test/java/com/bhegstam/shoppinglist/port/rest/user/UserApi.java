package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.port.rest.TestClientFactory;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class UserApi {
    private final String apiVersion;
    private final WebTarget webTarget;

    public UserApi(String apiVersion, String serviceUrl, String token) {
        this.apiVersion = apiVersion;
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }

    public Response getAuthenticatedUser() {
        return webTarget
                .path("user")
                .request()
                .accept(apiVersion)
                .get();
    }

    public Response getWorkspaces() {
        return webTarget
                .path("user")
                .path("workspace")
                .request()
                .accept(apiVersion)
                .get();
    }
}
