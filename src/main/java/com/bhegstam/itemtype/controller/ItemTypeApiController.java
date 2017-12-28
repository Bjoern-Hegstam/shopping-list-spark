package com.bhegstam.itemtype.controller;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.List;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;
import static java.util.stream.Collectors.toList;

public class ItemTypeApiController implements Controller {
    private final ItemTypeRepository itemTypeRepository;

    @Inject
    public ItemTypeApiController(ItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.path(Path.Api.ITEM_TYPE, () -> {
            http.post(
                    "/",
                    "application/json",
                    asSparkRoute(this::postItemType),
                    new JsonResponseTransformer()
            );
            http.get(
                    "",
                    asSparkRoute(this::findItemTypes),
                    new JsonResponseTransformer()
            );
        });
    }

    private Result postItemType(Request request) {
        ItemTypeBean itemTypeBean = ItemTypeBean.fromJson(request.body());

        ItemType itemType = itemTypeRepository.createItemType(itemTypeBean.getName());
        return result().returnPayload(ItemTypeBean.fromItemType(itemType));
    }

    private Result findItemTypes(Request request) {
        String nameStart = request.queryParams("name");
        int limit = Integer.parseInt(request.queryParams("limit"));

        List<ItemTypeBean> itemTypeBeans = itemTypeRepository
                .findItemTypes(nameStart, limit).stream()
                .map(ItemTypeBean::fromItemType)
                .collect(toList());

        return result()
                .type("application/json")
                .returnPayload(itemTypeBeans);
    }
}
