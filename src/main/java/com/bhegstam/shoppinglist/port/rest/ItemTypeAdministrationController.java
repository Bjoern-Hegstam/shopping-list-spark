package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Result;
import spark.Service;

import java.util.HashMap;
import java.util.Map;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;
import static java.util.stream.Collectors.toList;

public class ItemTypeAdministrationController implements Controller {
    private final ItemTypeRepository itemTypeRepository;

    public ItemTypeAdministrationController(ItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.get(
                Path.Web.ADMIN + "/" + Path.Web.ITEM_TYPES,
                asSparkRoute(request -> serveItemTypeList())
        );
    }

    private Result serveItemTypeList() {
        Map<String, Object> model = new HashMap<>();
        model.put(
                "itemTypes",
                itemTypeRepository
                        .getItemTypes().stream()
                        .map(ItemTypeBean::fromItemType)
                        .collect(toList())
        );
        return result().render(Path.Template.ADMIN_ITEM_TYPES, model);
    }
}
