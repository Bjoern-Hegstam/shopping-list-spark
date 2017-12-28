package com.bhegstam.itemtype.controller;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeId;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import com.bhegstam.util.Path;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.util.List;

import static com.bhegstam.util.ContentType.APPLICATION_JSON;
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
        http.post(
                Path.Api.ITEM_TYPE,
                APPLICATION_JSON,
                asSparkRoute(this::postItemType),
                new JsonResponseTransformer()
        );

        http.get(
                Path.Api.ITEM_TYPE,
                asSparkRoute(this::findItemTypes),
                new JsonResponseTransformer()
        );

        http.delete(
                Path.Api.ITEM_TYPE + "/:itemTypeId",
                asSparkRoute(this::deleteItemType)
        );
    }

    private Result postItemType(Request request) {
        ItemTypeBean itemTypeBean = ItemTypeBean.fromJson(request.body());

        ItemType itemType = itemTypeRepository.createItemType(itemTypeBean.getName());
        return result()
                .statusCode(HttpStatus.CREATED_201)
                .type(APPLICATION_JSON)
                .returnPayload(ItemTypeBean.fromItemType(itemType));
    }

    private Result findItemTypes(Request request) {
        String nameStart = request.queryParams("name");
        int limit = Integer.parseInt(request.queryParams("limit"));

        List<ItemTypeBean> itemTypeBeans = itemTypeRepository
                .findItemTypes(nameStart, limit).stream()
                .map(ItemTypeBean::fromItemType)
                .collect(toList());

        return result()
                .type(APPLICATION_JSON)
                .returnPayload(itemTypeBeans);
    }

    private Result deleteItemType(Request request) {
        ItemTypeId itemTypeId = ItemTypeId.fromString(request.params("itemTypeId"));
        itemTypeRepository.deleteItemType(itemTypeId);
        return result()
                .statusCode(HttpStatus.OK_200)
                .returnPayload(new Object());
    }
}
