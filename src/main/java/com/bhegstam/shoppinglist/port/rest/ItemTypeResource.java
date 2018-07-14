package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.application.ItemTypeApplication;
import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.Auth;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path("item-type")
@Produces(MediaType.APPLICATION_JSON)
public class ItemTypeResource {
    private final ItemTypeApplication itemTypeApplication;

    public ItemTypeResource(ItemTypeApplication itemTypeApplication) {
        this.itemTypeApplication = itemTypeApplication;
    }

    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postItemType(@Auth User user, @Valid CreateItemTypeRequest request) {
        ItemType itemType = itemTypeApplication.createItemType(request.getName());

        return Response
                .status(CREATED)
                .entity(new ItemTypeResponse(itemType))
                .build();
    }

    @RolesAllowed({USER, ADMIN})
    @GET
    public Response findItemTypes(@Auth User user, @QueryParam("name") String nameStart, @QueryParam("limit") Integer limit) {
        List<ItemType> itemTypes;
        if (nameStart == null && limit == null && user.isAdmin()) {
            itemTypes = itemTypeApplication.getItemTypes();
        } else {
            itemTypes = itemTypeApplication.findItemTypes(nameStart, limit);
        }

        return Response
                .status(OK)
                .entity(new ItemTypesResponse(itemTypes))
                .build();
    }

    @Path("/{item_type_id}")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response deleteItemType(@Auth User user, @PathParam("item_type_id") String itemTypeIdString) {
        ItemTypeId itemTypeId = ItemTypeId.parse(itemTypeIdString);

        itemTypeApplication.deleteItemType(itemTypeId);

        return Response
                .status(HttpStatus.NO_CONTENT_204)
                .build();
    }
}
