package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.port.rest.TestClientFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

class ShoppingListApi {
    private final String apiVersion;
    private final WebTarget webTarget;

    ShoppingListApi(String apiVersion, String serviceUrl, String token) {
        this.apiVersion = apiVersion;
        webTarget = TestClientFactory
                .createClient(token)
                .target(serviceUrl);
    }

    Response postShoppingList(String json) {
        return webTarget
                .path("shopping-list")
                .request()
                .accept(apiVersion)
                .post(Entity.json(json));
    }

    Response getShoppingList(String listId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .request()
                .accept(apiVersion)
                .get();
    }

    Response getShoppingLists() {
        return webTarget
                .path("shopping-list")
                .request()
                .accept(apiVersion)
                .get();
    }

    Response updateShoppingList(String listId, String json) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .request()
                .accept(apiVersion)
                .put(Entity.json(json));
    }

    Response deleteShoppingList(String listId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .request()
                .accept(apiVersion)
                .delete();
    }

    Response postItemType(String listId, String json) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item-type")
                .request()
                .accept(apiVersion)
                .post(Entity.json(json));
    }

    Response getItemTypes(String listId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item-type")
                .request()
                .accept(apiVersion)
                .get();
    }

    Response deleteItemType(String listId, String id) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item-type")
                .path(id)
                .request()
                .accept(apiVersion)
                .delete();
    }

    Response postShoppingListItem(String listId, String json) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item")
                .request()
                .accept(apiVersion)
                .post(Entity.json(json));
    }

    Response putShoppingListItem(String listId, String listItemId, String json) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item")
                .path(listItemId)
                .request()
                .accept(apiVersion)
                .put(Entity.json(json));
    }

    Response deleteShoppingListItem(String listId, String listItemId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("item")
                .path(listItemId)
                .request()
                .accept(apiVersion)
                .delete();
    }

    Response deleteShoppingListCart(String listId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("cart")
                .request()
                .accept(apiVersion)
                .delete();
    }

    public Response addToCart(String listId, String json) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("cart")
                .path("item")
                .request()
                .accept(apiVersion)
                .post(Entity.json(json));
    }

    public Response removeFromCart(String listId, String listItemId) {
        return webTarget
                .path("shopping-list")
                .path(listId)
                .path("cart")
                .path("item")
                .path(listItemId)
                .request()
                .accept(apiVersion)
                .delete();
    }
}
