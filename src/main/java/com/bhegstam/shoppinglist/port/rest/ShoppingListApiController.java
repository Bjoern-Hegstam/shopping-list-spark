package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.application.ShoppingListApplication;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.util.List;

import static com.bhegstam.shoppinglist.port.rest.ContentType.APPLICATION_JSON;
import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class ShoppingListApiController implements Controller {
    private static final Logger LOGGER = LogManager.getLogger(ShoppingListApiController.class);

    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String SHOPPING_LIST_ITEM_ID = "shoppingListItemId";

    private final ShoppingListApplication shoppingListApplication;

    public ShoppingListApiController(ShoppingListApplication shoppingListApplication) {
        this.shoppingListApplication = shoppingListApplication;
    }

    @Override
    public void configureRoutes(Service http) {
        http.get(
                Path.Api.SHOPPING_LIST,
                APPLICATION_JSON,
                asSparkRoute(this::getShoppingLists),
                new JsonResponseTransformer()
        );

        http.post(
                Path.Api.SHOPPING_LIST,
                APPLICATION_JSON,
                asSparkRoute(this::postShoppingList),
                new JsonResponseTransformer()
        );

        http.get(
                Path.Api.SHOPPING_LIST + "/:" + SHOPPING_LIST_ID,
                APPLICATION_JSON,
                asSparkRoute(this::getShoppingList),
                new JsonResponseTransformer()
        );

        http.post(
                Path.Api.SHOPPING_LIST + "/:" + SHOPPING_LIST_ID + "/item",
                APPLICATION_JSON,
                asSparkRoute(this::postShoppingListItem),
                new JsonResponseTransformer()
        );

        http.patch(
                Path.Api.SHOPPING_LIST + "/:" + SHOPPING_LIST_ID + "/item/:" + SHOPPING_LIST_ITEM_ID,
                APPLICATION_JSON,
                asSparkRoute(this::patchShoppingListItem)
        );

        http.delete(
                Path.Api.SHOPPING_LIST + "/:" + SHOPPING_LIST_ID + "/item/:" + SHOPPING_LIST_ITEM_ID,
                asSparkRoute(this::deleteShoppingListItem)
        );

        http.delete(
                Path.Api.SHOPPING_LIST + "/:" + SHOPPING_LIST_ID + "/cart",
                asSparkRoute(this::emptyCart)
        );
    }

    Result getShoppingLists(Request request) {
        List<ShoppingList> shoppingLists = shoppingListApplication.getShoppingLists();

        return result()
                .statusCode(HttpStatus.OK_200)
                .type(APPLICATION_JSON)
                .returnPayload(new GetShoppingListsResponse(shoppingLists));
    }

    Result getShoppingList(Request request) {
        String id = request.queryParams(SHOPPING_LIST_ID);
        ShoppingList shoppingList;
        try {
            shoppingList = shoppingListApplication.get(ShoppingListId.fromString(id));
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Could not find shopping list with id [{}]", id);
            return result()
                    .statusCode(HttpStatus.BAD_REQUEST_400)
                    .type(APPLICATION_JSON)
                    .returnPayload(null);
        }

        return result()
                .statusCode(HttpStatus.OK_200)
                .type(APPLICATION_JSON)
                .returnPayload(new GetShoppingListResponse(shoppingList));
    }

    Result postShoppingList(Request request) {
        CreateShoppingListRequest shoppingListRequest = CreateShoppingListRequest.fromJson(request.body());

        ShoppingList shoppingList = shoppingListApplication.createShoppingList(shoppingListRequest.getName());

        return result()
                .statusCode(HttpStatus.CREATED_201)
                .type(APPLICATION_JSON)
                .returnPayload(new CreateShoppingListResponse(shoppingList.getId()));
    }

    private Result postShoppingListItem(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));
        ShoppingListItemBean itemBean = ShoppingListItemBean.fromJson(request.body());
        ItemTypeId itemTypeId = new ItemTypeId(itemBean.getItemType().getId());

        ShoppingListItem listItem = shoppingListApplication.addItem(listId, itemBean, itemTypeId);

        return result()
                .statusCode(HttpStatus.CREATED_201)
                .type(APPLICATION_JSON)
                .returnPayload(new ShoppingListItemBean(listItem));
    }

    private Result patchShoppingListItem(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));
        ShoppingListItemId listItemId = ShoppingListItemId.fromString(request.params(SHOPPING_LIST_ITEM_ID));
        ShoppingListItemBean itemBean = ShoppingListItemBean.fromJson(request.body());

        shoppingListApplication.updateItem(listId, listItemId, itemBean);

        return result()
                .statusCode(HttpStatus.NO_CONTENT_204)
                .returnPayload(new Object());
    }

    private Result deleteShoppingListItem(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));
        ShoppingListItemId listItemId = ShoppingListItemId.fromString(request.params(SHOPPING_LIST_ITEM_ID));

        shoppingListApplication.deleteItem(listId, listItemId);

        return result()
                .statusCode(HttpStatus.NO_CONTENT_204)
                .returnPayload(new Object());
    }

    private Result emptyCart(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));

        shoppingListApplication.emptyCart(listId);

        return result()
                .statusCode(HttpStatus.NO_CONTENT_204)
                .returnPayload(new Object());
    }
}
