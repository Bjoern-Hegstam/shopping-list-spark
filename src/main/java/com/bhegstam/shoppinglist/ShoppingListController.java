package com.bhegstam.shoppinglist;

import com.bhegstam.util.Path;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;
import java.util.Map;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class ShoppingListController implements Controller {
    private final ShoppingListRepository shoppingListRepository;

    @Inject
    public ShoppingListController(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.get(Path.Web.SHOPPING_LIST, asSparkRoute(request -> serveListOfShoppingLists()));
    }

    private Result serveListOfShoppingLists() {
        Map<String, Object> model = new HashMap<>();
        model.put("shoppingLists", shoppingListRepository.getShoppingLists());

        return result().render(Path.Template.SHOPPING_LISTS, model);
    }
}
