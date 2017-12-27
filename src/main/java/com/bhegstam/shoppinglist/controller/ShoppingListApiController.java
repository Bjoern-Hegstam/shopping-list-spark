package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.Filters;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class ShoppingListApiController implements Controller {
    private final ShoppingListRepository shoppingListRepository;

    @Inject
    public ShoppingListApiController(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.path(Path.Api.SHOPPING_LIST, () -> {
            http.before("/*", Filters::userIsLoggedIn);
            http.post(
                    "/",
                    "application/json",
                    asSparkRoute(this::postShoppingList),
                    new JsonResponseTransformer()
            );
        });
    }

    private Result postShoppingList(Request request) {
        ShoppingListBean shoppingListBean = ShoppingListBean.fromJson(request.body());

        ShoppingList shoppingList = shoppingListRepository.createShoppingList(shoppingListBean.getName());
        return result().returnPayload(ShoppingListBean.fromShoppingList(shoppingList));
    }
}
