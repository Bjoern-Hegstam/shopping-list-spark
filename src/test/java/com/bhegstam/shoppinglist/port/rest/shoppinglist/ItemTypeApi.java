package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.port.rest.TestClientFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

class ItemTypeApi {
    private final String apiVersion;
    private final WebTarget webTarget;

    ItemTypeApi(String apiVersion, String serviceUrl, String token) {
        this.apiVersion = apiVersion;
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }

    Response postItemType(String json) {
        return webTarget
                .path("item-type")
                .request()
                .accept(apiVersion)
                .post(Entity.json(json));
    }

    Response getItemTypes() {
        return webTarget
                .path("item-type")
                .request()
                .accept(apiVersion)
                .get();
    }

    Response deleteItemType(String id) {
        return webTarget
                .path("item-type")
                .path(id)
                .request()
                .accept(apiVersion)
                .delete();
    }
}
