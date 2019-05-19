package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.application.ShoppingListApplication;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.port.persistence.ItemTypeNotFoundException;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;
import static com.bhegstam.shoppinglist.port.rest.shoppinglist.RestApiMimeType.SHOPPING_LIST_1_0;
import static javax.ws.rs.core.Response.Status.*;

@Path("shopping-list")
@Produces(SHOPPING_LIST_1_0)
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
        LOGGER.info("Received request [{}] to create shopping list for user [{}]", request, user.getId());

        ShoppingList shoppingList = shoppingListApplication.createShoppingList(request.getName());

        return createAndLogResponse(CREATED, new CreateShoppingListResponse(shoppingList.getId()));
    }

    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getShoppingLists(@Auth User user) {
        LOGGER.info("Received request to get shopping lists for user [{}]", user.getId());

        List<ShoppingList> shoppingLists = shoppingListApplication.getShoppingLists();

        return createAndLogResponse(OK, new GetShoppingListsResponse(shoppingLists));
    }

    @Path("/{shoppingListId}")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getShoppingList(@Auth User user, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString) {
        LOGGER.info("Received request to get shopping list with id [{}] for user [{}]", shoppingListIdString, user.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while getting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            ShoppingList shoppingList = shoppingListApplication.getShoppingList(listId);

            return createAndLogResponse(OK, new GetShoppingListResponse(shoppingList));
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while getting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @Path("/{shoppingListId}")
    @RolesAllowed({USER, ADMIN})
    @PUT
    public Response updateShoppingList(@Auth User user, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString, @Valid UpdateShoppingListRequest request) {
        LOGGER.info("Received request [{}] to update shopping list with id [{}] for user [{}]", request, shoppingListIdString, user.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while updating shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.updateShoppingList(listId, request.getName());

            return createAndLogResponse(NO_CONTENT, null);
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while getting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @Path("/{shoppingListId}")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response deleteShoppingList(@Auth User user, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString) {
        LOGGER.info("Received request to delete shopping list with id [{}] for user [{}]", shoppingListIdString, user.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.deleteShoppingList(listId);

            return createAndLogResponse(NO_CONTENT, null);
        } catch (ShoppingListDeleteNotAllowedException e) {
            LOGGER.error("Error while deleting shopping list with id [" + shoppingListIdString + "]");
            return Response.status(BAD_REQUEST).build();
        }
    }

    @Path("/{shoppingListId}/item")
    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postShoppingListItem(@Auth User user, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString, @Valid CreateShoppingListItemRequest request) {
        LOGGER.info("Received request [{}] to create shopping list item in shopping list [{}] for user [{}]", request, shoppingListIdString, user.getId());

        ShoppingListId listId;
        ItemTypeId itemTypeId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
            itemTypeId = ItemTypeId.parse(request.getItemTypeId());
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.error("Error while adding item to shopping list [" + shoppingListIdString + "] for user [" + user.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            ShoppingListItem listItem = shoppingListApplication.addItem(listId, itemTypeId, request.getQuantity());

            return createAndLogResponse(CREATED, new ShoppingListItemResponse(listItem));
        } catch (ItemTypeNotFoundException | ShoppingListNotFoundException e) {
            LOGGER.error("Error while adding item of type [" + itemTypeId + "] to shopping list [" + listId + "] for user [" + user.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @Path("/{shoppingListId}/item/{shoppingListItemId}")
    @RolesAllowed({USER, ADMIN})
    @PUT
    public Response putShoppingListItem(
            @Auth User user,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString,
            @PathParam(SHOPPING_LIST_ITEM_ID) String shoppingListItemIdString,
            @Valid UpdateShoppingListItemRequest request
    ) {
        LOGGER.info("Received request [{}] to update shopping list item [{}] in shopping list [{}] for user [{}]", request, shoppingListItemIdString, shoppingListIdString, user.getId());

        ShoppingListId listId;
        ShoppingListItemId listItemId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
            listItemId = ShoppingListItemId.fromString(shoppingListItemIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while updating item [" + shoppingListItemIdString + "] in shopping list [" + shoppingListIdString + "] for user [" + user.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.updateItem(listId, listItemId, request.getQuantity(), request.isInCart());

            return createAndLogResponse(NO_CONTENT, null);
        } catch (ShoppingListNotFoundException | ShoppingListItemNotFoundException e) {
            LOGGER.error("Error while updating item [" + listItemId + "] in shopping list [" + listId + "] for user [" + user.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
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
        LOGGER.info("Received request to delete shopping list item [{}] in shopping list [{}] for user [{}]", shoppingListItemIdString, shoppingListIdString, user.getId());

        ShoppingListId listId;
        ShoppingListItemId listItemId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
            listItemId = ShoppingListItemId.fromString(shoppingListItemIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting item [" + shoppingListItemIdString + "] in shopping list [" + shoppingListIdString + "] for user [" + user.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.deleteItem(listId, listItemId);

            return createAndLogResponse(NO_CONTENT, null);
        } catch (ShoppingListNotFoundException | ShoppingListItemNotFoundException e) {
            LOGGER.error("Error while deleting item [" + listItemId + "] in shopping list [" + listId + "] for user [" + user.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @Path("/{shoppingListId}/cart")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response emptyCart(
            @Auth User user,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString
    ) {
        LOGGER.info("Received request to empty cart of shopping list [{}] for user [{}]", shoppingListIdString, user.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.fromString(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while emptying card of shopping list [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.emptyCart(listId);

            return createAndLogResponse(NO_CONTENT, null);
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while emptying card of shopping list [" + listId + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    private Response createAndLogResponse(Response.Status status, Object body) {
        LOGGER.info("Responding to request with status [{}] and body [{}]", status, body);
        return Response.status(status).entity(body).build();
    }
}
