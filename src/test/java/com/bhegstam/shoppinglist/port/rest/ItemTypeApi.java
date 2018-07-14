package com.bhegstam.shoppinglist.port.rest;

import javax.ws.rs.client.WebTarget;

class ItemTypeApi {
    private final WebTarget webTarget;

    ItemTypeApi(String token, String serviceUrl) {
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }
}
