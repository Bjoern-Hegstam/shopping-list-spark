package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.application.ShoppingListApplication;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.port.persistence.ItemTypeNotFoundException;
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

@Path("shopping-list")
@Produces(MediaType.APPLICATION_JSON)
public class ShoppingListResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingListResource.class);

    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String SHOPPING_LIST_ITEM_ID = "shoppingListItemId";

    private final ShoppingListApplication shoppingListApplication;

    public ShoppingListResource(ShoppingListApplication shoppingListApplication) {
        this.shoppingListApplication = shoppingListApplication;
    }

    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postShoppingList(@Auth User user, @Valid CreateShoppingListRequest request) {
        ShoppingList shoppingList = shoppingListApplication.createShoppingList(request.getName());

        return Response
                .status(CREATED)
                .entity(new CreateShoppingListResponse(shoppingList.getId()))
                .build();
    }

    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getShoppingLists(@Auth User user) {
        List<ShoppingList> shoppingLists = shoppingListApplication.getShoppingLists();

        return Response
                .status(OK)
                .entity(new GetShoppingListsResponse(shoppingLists))
                .build();
    }

    @Path("/{shoppingListId}")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getShoppingList(@Auth User user, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString) {
        LOGGER.info("Getting shopping list with id {} for user {}", shoppingListIdString, user.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while getting shopping list with id " + shoppingListIdString, e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        try {
            ShoppingList shoppingList = shoppingListApplication.get(listId);

            return Response
                    .status(OK)
                    .entity(new GetShoppingListResponse(shoppingList))
                    .build();
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while getting shopping list with id " + shoppingListIdString, e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }
    }

    @Path("/{shoppingListId}/item")
    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postShoppingListItem(@Auth User user, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString, @Valid CreateShoppingListItemRequest request) {
        ShoppingListId listId;
        ItemTypeId itemTypeId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
            itemTypeId = ItemTypeId.parse(request.getItemTypeId());
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.error("Error while adding item to shopping list " + shoppingListIdString + " for user " + user.getId(), e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        try {
            ShoppingListItem listItem = shoppingListApplication.addItem(listId, itemTypeId, request.getQuantity());

            return Response
                    .status(CREATED)
                    .entity(new ShoppingListItemResponse(listItem))
                    .build();
        } catch (ItemTypeNotFoundException | ShoppingListNotFoundException e) {
            LOGGER.error("Error while adding item of type " + itemTypeId + " to shopping list " + listId + " for user " + user.getId(), e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }
    }

    @Path("/{shoppingListId}/item/{shoppingListItemId}")
    @RolesAllowed({USER, ADMIN})
    @PUT
    public Response patchShoppingListItem(
            @Auth User user,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString,
            @PathParam(SHOPPING_LIST_ITEM_ID) String shoppingListItemIdString,
            @Valid UpdateShoppingListItemRequest request
    ) {
        ShoppingListId listId;
        ShoppingListItemId listItemId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
            listItemId = ShoppingListItemId.fromString(shoppingListItemIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while updating item " + shoppingListItemIdString + " in shopping list " + shoppingListIdString + " for user " + user.getId(), e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        try {
            shoppingListApplication.updateItem(listId, listItemId, request.getQuantity(), request.isInCart());

            return Response
                    .status(NO_CONTENT)
                    .build();
        } catch (ShoppingListNotFoundException | ShoppingListItemNotFoundException e) {
            LOGGER.error("Error while updating item " + listItemId + " in shopping list " + listId + " for user " + user.getId(), e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }
    }

    @Path("/{shoppingListId}/item/{shoppingListItemId}")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response deleteShoppingListItem(
            @Auth User user,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString,
            @PathParam(SHOPPING_LIST_ITEM_ID) String shoppingListItemIdString
    ) {
        ShoppingListId listId;
        ShoppingListItemId listItemId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
            listItemId = ShoppingListItemId.fromString(shoppingListItemIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting item " + shoppingListItemIdString + " in shopping list " + shoppingListIdString + " for user " + user.getId(), e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        try {
            shoppingListApplication.deleteItem(listId, listItemId);

            return Response
                    .status(NO_CONTENT)
                    .build();
        } catch (ShoppingListNotFoundException | ShoppingListItemNotFoundException e) {
            LOGGER.error("Error while deleting item " + listItemId + " in shopping list " + listId + " for user " + user.getId(), e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }
    }

    @Path("/{shoppingListId}/cart")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response emptyCart(
            @Auth User user,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString
    ) {
        ShoppingListId listId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while emptying card of shopping list " + shoppingListIdString, e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        try {
            shoppingListApplication.emptyCart(listId);

            return Response
                    .status(NO_CONTENT)
                    .build();
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while emptying card of shopping list " + listId, e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }
    }
}
