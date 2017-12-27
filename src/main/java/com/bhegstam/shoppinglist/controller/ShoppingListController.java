package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.bhegstam.shoppinglist.domain.ShoppingListId;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.Filters;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;
import java.util.Map;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;
import static java.util.stream.Collectors.toList;

public class ShoppingListController implements Controller {
    private final ShoppingListRepository shoppingListRepository;

    @Inject
    public ShoppingListController(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.path(Path.Web.SHOPPING_LIST, () -> {
            http.before("/*", Filters.userIsLoggedIn(Filters.Actions.redirectNotAuthorized(Path.Web.INDEX)));
            http.get("/", asSparkRoute(request -> serveListOfShoppingLists()));
            http.get("/:shoppingListId/", asSparkRoute(this::getShoppingList));
        });
    }

    private Result serveListOfShoppingLists() {
        Map<String, Object> model = new HashMap<>();
        model.put(
                "shoppingLists",
                shoppingListRepository
                        .getShoppingLists().stream()
                        .map(ShoppingListBean::fromShoppingList)
                        .collect(toList())
        );

        return result().render(Path.Template.SHOPPING_LISTS, model);
    }

    private Result getShoppingList(Request request) {
        ShoppingListId listId = ShoppingListId.fromString(request.params("shoppingListId"));
        ShoppingList shoppingList = shoppingListRepository.get(listId);

        Map<String, Object> model = new HashMap<>();
        model.put("shoppingList", ShoppingListBean.fromShoppingList(shoppingList));
        return result().render(Path.Template.SHOPPING_LIST, model);
    }
}
