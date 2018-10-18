package com.bhegstam.shoppinglist.port.rest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

class ItemTypeApi {
    private final WebTarget webTarget;

    ItemTypeApi(String token, String serviceUrl) {
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }

    Response postItemType(String json) {
        return webTarget
                .path("item-type")
                .request()
                .post(Entity.json(json));
    }

    Response getItemTypes() {
        return webTarget
                .path("item-type")
                .request()
                .get();
    }

    Response deleteItemType(String id) {
        return webTarget
                .path("item-type")
                .path(id)
                .request()
                .delete();
    }
}
