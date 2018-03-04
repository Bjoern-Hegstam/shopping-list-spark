package com.bhegstam.shoppinglist.controller;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeId;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.util.List;
import java.util.Optional;

import static com.bhegstam.util.ContentType.APPLICATION_JSON;
import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class ShoppingListApiController implements Controller {
    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String SHOPPING_LIST_ITEM_ID = "shoppingListItemId";

    private final ShoppingListRepository shoppingListRepository;
    private final ItemTypeRepository itemTypeRepository;

    @Inject
    public ShoppingListApiController(
            ShoppingListRepository shoppingListRepository,
            ItemTypeRepository itemTypeRepository
    ) {
        this.shoppingListRepository = shoppingListRepository;
        this.itemTypeRepository = itemTypeRepository;
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
        List<ShoppingList> shoppingLists = shoppingListRepository.getShoppingLists();

        return result()
                .statusCode(HttpStatus.OK_200)
                .type(APPLICATION_JSON)
                .returnPayload(new GetShoppingListsResponse(shoppingLists));
    }

    Result postShoppingList(Request request) {
        CreateShoppingListRequest shoppingListRequest = CreateShoppingListRequest.fromJson(request.body());

        ShoppingList shoppingList = shoppingListRepository.createShoppingList(shoppingListRequest.getName());

        return result()
                .statusCode(HttpStatus.CREATED_201)
                .type(APPLICATION_JSON)
                .returnPayload(new CreateShoppingListResponse(shoppingList.getId()));
    }

    Result postShoppingListItem(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));
        ShoppingListItemBean itemBean = ShoppingListItemBean.fromJson(request.body());
        ItemTypeId itemTypeId = new ItemTypeId(itemBean.getItemType().getId());

        ItemType itemType = itemTypeRepository.get(itemTypeId);

        ShoppingList shoppingList = shoppingListRepository.get(listId);
        ShoppingListItem listItem = shoppingList.add(itemType);
        listItem.setQuantity(itemBean.getQuantity());

        shoppingListRepository.update(shoppingList);

        return result()
                .statusCode(HttpStatus.CREATED_201)
                .type(APPLICATION_JSON)
                .returnPayload(ShoppingListItemBean.fromShoppingListItem(listItem));
    }

    Result patchShoppingListItem(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));
        ShoppingListItemId listItemId = ShoppingListItemId.fromString(request.params(SHOPPING_LIST_ITEM_ID));
        ShoppingListItemBean itemBean = ShoppingListItemBean.fromJson(request.body());

        ShoppingList shoppingList = shoppingListRepository.get(listId);

        ShoppingListItem listItem = shoppingList.get(listItemId);

        Optional.ofNullable(itemBean.getQuantity()).ifPresent(listItem::setQuantity);
        Optional.ofNullable(itemBean.getInCart()).ifPresent(listItem::setInCart);

        shoppingListRepository.update(shoppingList);

        return result()
                .statusCode(HttpStatus.NO_CONTENT_204)
                .returnPayload(new Object());
    }

    Result deleteShoppingListItem(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));
        ShoppingListItemId listItemId = ShoppingListItemId.fromString(request.params(SHOPPING_LIST_ITEM_ID));

        ShoppingList shoppingList = shoppingListRepository.get(listId);

        shoppingList.remove(listItemId);

        shoppingListRepository.update(shoppingList);

        return result()
                .statusCode(HttpStatus.NO_CONTENT_204)
                .returnPayload(new Object());
    }

    Result emptyCart(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params(SHOPPING_LIST_ID));

        ShoppingList shoppingList = shoppingListRepository.get(listId);

        shoppingList.removeItemsInCart();

        shoppingListRepository.update(shoppingList);

        return result()
                .statusCode(HttpStatus.NO_CONTENT_204)
                .returnPayload(new Object());
    }
}
