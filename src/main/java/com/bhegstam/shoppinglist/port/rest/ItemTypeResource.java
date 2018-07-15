package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.application.ItemTypeApplication;
import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;
import static javax.ws.rs.core.Response.Status.*;

@Path("item-type")
@Produces(MediaType.APPLICATION_JSON)
public class ItemTypeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListResource.class);

    private final ItemTypeApplication itemTypeApplication;

    public ItemTypeResource(ItemTypeApplication itemTypeApplication) {
        this.itemTypeApplication = itemTypeApplication;
    }

    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postItemType(@Auth User user, @Valid CreateItemTypeRequest request) {
        LOGGER.info("Received request [{}] to create item type for user [{}]", request, user.getId());

        ItemType itemType = itemTypeApplication.createItemType(request.getName());

        Response.Status status = CREATED;
        ItemTypeResponse body = new ItemTypeResponse(itemType);

        logResponse(status, body);

        return Response
                .status(status)
                .entity(body)
                .build();
    }

    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getItemTypes(@Auth User user, @QueryParam("name") String nameStart, @QueryParam("limit") Integer limit) {
        LOGGER.info("Received request to get item types [nameStart: [{}], limit: [{}]] for user [{}]", nameStart, limit, user.getId());

        List<ItemType> itemTypes;
        if (nameStart == null && limit == null) {
            itemTypes = itemTypeApplication.getItemTypes();
        } else {
            itemTypes = itemTypeApplication.findItemTypes(nameStart, limit);
        }

        Response.Status status = OK;
        ItemTypesResponse body = new ItemTypesResponse(itemTypes);

        logResponse(status, body);

        return Response
                .status(status)
                .entity(body)
                .build();
    }

    @Path("/{item_type_id}")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response deleteItemType(@Auth User user, @PathParam("item_type_id") String itemTypeIdString) {
        LOGGER.info("Received request to delete item type [{}] for user [{}]", itemTypeIdString, user.getId());

        ItemTypeId itemTypeId;
        try {
            itemTypeId = ItemTypeId.parse(itemTypeIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting item type [" + itemTypeIdString + "] for user [" + user.getId() + "]", e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        itemTypeApplication.deleteItemType(itemTypeId);

        Response.Status status = NO_CONTENT;

        logResponse(status, null);

        return Response
                .status(status)
                .build();
    }

    private void logResponse(Response.Status status, Object body) {
        LOGGER.info("Responding to request with status [{}] and body [{}]", status, body);
    }
}
