package com.bhegstam.shoppinglist.controller;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeId;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.bhegstam.shoppinglist.domain.ShoppingListId;
import com.bhegstam.shoppinglist.domain.ShoppingListItem;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class ShoppingListApiController implements Controller {
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
        http.post(
                Path.Api.SHOPPING_LIST,
                "application/json",
                asSparkRoute(this::postShoppingList),
                new JsonResponseTransformer()
        );

        http.post(
                Path.Api.SHOPPING_LIST + "/:shoppingListId/item",
                "application/json",
                asSparkRoute(this::postShoppingListItem),
                new JsonResponseTransformer()
        );
    }

    private Result postShoppingList(Request request) {
        ShoppingListBean shoppingListBean = ShoppingListBean.fromJson(request.body());

        ShoppingList shoppingList = shoppingListRepository.createShoppingList(shoppingListBean.getName());
        return result().returnPayload(ShoppingListBean.fromShoppingList(shoppingList));
    }

    private Result postShoppingListItem(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params("shoppingListId"));
        ShoppingListItemBean itemBean = ShoppingListItemBean.fromJson(request.body());
        ItemTypeId itemTypeId = new ItemTypeId(itemBean.getItemType().getId());

        ItemType itemType = itemTypeRepository.get(itemTypeId);

        ShoppingList shoppingList = shoppingListRepository.get(listId);
        ShoppingListItem listItem = shoppingList.add(itemType);
        listItem.setQuantity(itemBean.getQuantity());

        shoppingListRepository.update(shoppingList);

        return result()
                .statusCode(HttpStatus.OK_200)
                .returnPayload(ShoppingListBean.fromShoppingList(shoppingList));
    }
}
