package com.bhegstam.shoppinglist.port.rest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

class ShoppingListApi {
    private final WebTarget webTarget;

    ShoppingListApi(String token, String serviceUrl) {
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }

    Response addShoppingList(String json) {
        webTarget
                .path("shopping-list")
                .request()
                .post(Entity.json(json));
    }

    Response getShoppingList(String listId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .request()
                .get();
    }

    Response getShoppingLists() {
        return webTarget
                .path("shopping-list")
                .request()
                .get();
    }

    Response addShoppingListItem(String listId, String json) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item")
                .request()
                .post(Entity.json(json));
    }

    Response updateShoppingListItem(String listId, String listItemId, String json) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item")
                .path(listItemId)
                .request()
                .put(Entity.json(json));
    }

    Response deleteShoppingListItem(String listId, String listItemId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item")
                .path(listItemId)
                .request()
                .delete();
    }

    Response emptyShoppingListCart(String listId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("cart")
                .request()
                .delete();
    }
}
