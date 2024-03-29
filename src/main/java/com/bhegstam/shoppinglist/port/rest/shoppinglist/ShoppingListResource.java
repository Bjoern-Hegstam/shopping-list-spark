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

    private static final int UNPROCESSABLE_ENTITY = 422;
    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String SHOPPING_LIST_ITEM_ID = "shoppingListItemId";

    private final ShoppingListApplication shoppingListApplication;

    public ShoppingListResource(ShoppingListApplication shoppingListApplication) {
        this.shoppingListApplication = shoppingListApplication;
    }

    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postShoppingList(@Auth User authenticatedUser, @Valid CreateShoppingListRequest request) {
        LOGGER.info("Received request [{}] to create shopping list for user [{}]", request, authenticatedUser.getId());

        ShoppingList shoppingList = shoppingListApplication.createShoppingList(authenticatedUser.getId(), request.getName());

        return createAndLogResponse(CREATED, new CreateShoppingListResponse(shoppingList.getId()));
    }

    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getShoppingLists(@Auth User authenticatedUser) {
        LOGGER.info("Received request to get shopping lists for user [{}]", authenticatedUser.getId());

        List<ShoppingList> shoppingLists = shoppingListApplication.getShoppingLists(authenticatedUser.getId());

        return createAndLogResponse(OK, new GetShoppingListsResponse(shoppingLists));
    }

    @Path("{shoppingListId}")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getShoppingList(@Auth User authenticatedUser, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString) {
        LOGGER.info("Received request to get shopping list with id [{}] for user [{}]", shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while getting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            ShoppingList shoppingList = shoppingListApplication.getShoppingList(authenticatedUser.getId(), listId);

            return createAndLogResponse(OK, new GetShoppingListResponse(shoppingList));
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while getting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
    }

    @Path("{shoppingListId}")
    @RolesAllowed({USER, ADMIN})
    @PUT
    public Response updateShoppingList(@Auth User authenticatedUser, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString, @Valid UpdateShoppingListRequest request) {
        LOGGER.info("Received request [{}] to update shopping list with id [{}] for user [{}]", request, shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while updating shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.updateShoppingList(authenticatedUser.getId(), listId, request.getName());

            return createAndLogResponse(NO_CONTENT);
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while getting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
    }

    @Path("{shoppingListId}")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response deleteShoppingList(@Auth User authenticatedUser, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString) {
        LOGGER.info("Received request to delete shopping list with id [{}] for user [{}]", shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.deleteShoppingList(authenticatedUser.getId(), listId);

            return createAndLogResponse(NO_CONTENT);
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Could not find shopping list with id [" + shoppingListIdString + "]");
            return Response.status(NOT_FOUND).build();
        } catch (ShoppingListDeleteNotAllowedException e) {
            LOGGER.error("Error while deleting shopping list with id [" + shoppingListIdString + "]");
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
    }

    @Path("{shoppingListId}/item-type")
    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postItemType(
            @Auth User authenticatedUser,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString,
            @Valid CreateItemTypeRequest request
    ) {
        LOGGER.info("Received request [{}] to create item type for shopping list [{}] by user [{}]", request, shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        Response.Status status;
        Object body;
        try {
            ItemType itemType = shoppingListApplication.createItemType(authenticatedUser.getId(), listId, request.getName());

            status = CREATED;
            body = new ItemTypeResponse(itemType);
        } catch (ItemTypeNameAlreadyTakenException e) {
            status = CONFLICT;
            body = ErrorResponse.exception(e);
        }

        return createAndLogResponse(status, body);
    }

    @Path("{shoppingListId}/item-type")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getItemTypes(
            @Auth User authenticatedUser,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString
    ) {
        LOGGER.info("Received request to get item types for shopping list [{}] by user [{}]", shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        List<ItemType> itemTypes = shoppingListApplication.getItemTypes(authenticatedUser.getId(), listId);

        return createAndLogResponse(OK, new ItemTypesResponse(itemTypes));
    }

    @Path("{shoppingListId}/item-type/{item_type_id}")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response deleteItemType(
            @Auth User authenticatedUser,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString,
            @PathParam("item_type_id") String itemTypeIdString
    ) {
        LOGGER.info("Received request to delete item type [{}] for shopping list [{}] by user [{}]", itemTypeIdString, shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting shopping list with id [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        ItemTypeId itemTypeId;
        try {
            itemTypeId = ItemTypeId.parse(itemTypeIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting item type [" + itemTypeIdString + "] for user [" + authenticatedUser.getId() + "]", e);
            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        Response.Status status;
        Object body = null;
        try {
            shoppingListApplication.deleteItemType(authenticatedUser.getId(), listId, itemTypeId);
            status = NO_CONTENT;
        } catch (ItemTypeUsedInShoppingListException e) {
            status = CONFLICT;
            body = ErrorResponse.exception(e);
        }

        return createAndLogResponse(status, body);
    }

    @Path("{shoppingListId}/item")
    @RolesAllowed({USER, ADMIN})
    @POST
    public Response postShoppingListItem(@Auth User authenticatedUser, @PathParam(SHOPPING_LIST_ID) String shoppingListIdString, @Valid CreateShoppingListItemRequest request) {
        LOGGER.info("Received request [{}] to create shopping list item in shopping list [{}] for user [{}]", request, shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        ItemTypeId itemTypeId;
        String itemTypeName = request.getItemTypeName();
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
            itemTypeId = request.getItemTypeId() != null ? ItemTypeId.parse(request.getItemTypeId()) : null;
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.error("Error while adding item to shopping list [" + shoppingListIdString + "] for user [" + authenticatedUser.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            ShoppingListItem listItem;
            if (itemTypeId != null) {
                listItem = shoppingListApplication.addItem(authenticatedUser.getId(), listId, itemTypeId, request.getQuantity());
            } else {
                listItem = shoppingListApplication.addItem(authenticatedUser.getId(), listId, itemTypeName, request.getQuantity());
            }

            return createAndLogResponse(CREATED, new ShoppingListItemResponse(listItem));
        } catch (ItemTypeNotFoundException | ShoppingListNotFoundException e) {
            LOGGER.error("Error while adding item of type [" + itemTypeId + "] to shopping list [" + listId + "] for user [" + authenticatedUser.getId() + "]", e);
            return Response.status(UNPROCESSABLE_ENTITY).build();
        }
    }

    @Path("{shoppingListId}/item/{shoppingListItemId}")
    @RolesAllowed({USER, ADMIN})
    @PUT
    public Response putShoppingListItem(
            @Auth User authenticatedUser,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString,
            @PathParam(SHOPPING_LIST_ITEM_ID) String shoppingListItemIdString,
            @Valid UpdateShoppingListItemRequest request
    ) {
        LOGGER.info("Received request [{}] to update shopping list item [{}] in shopping list [{}] for user [{}]", request, shoppingListItemIdString, shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        ShoppingListItemId listItemId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
            listItemId = ShoppingListItemId.parse(shoppingListItemIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while updating item [" + shoppingListItemIdString + "] in shopping list [" + shoppingListIdString + "] for user [" + authenticatedUser.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            // TODO: Migrate calls to add item to cart to new resource, then remove inCart from request
            shoppingListApplication.updateItem(authenticatedUser.getId(), listId, listItemId, request.getQuantity(), request.isInCart());

            return createAndLogResponse(NO_CONTENT);
        } catch (ShoppingListNotFoundException | ShoppingListItemNotFoundException e) {
            LOGGER.error("Error while updating item [" + listItemId + "] in shopping list [" + listId + "] for user [" + authenticatedUser.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @Path("{shoppingListId}/item/{shoppingListItemId}")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response deleteShoppingListItem(
            @Auth User authenticatedUser,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString,
            @PathParam(SHOPPING_LIST_ITEM_ID) String shoppingListItemIdString
    ) {
        LOGGER.info("Received request to delete shopping list item [{}] in shopping list [{}] for user [{}]", shoppingListItemIdString, shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        ShoppingListItemId listItemId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
            listItemId = ShoppingListItemId.parse(shoppingListItemIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while deleting item [" + shoppingListItemIdString + "] in shopping list [" + shoppingListIdString + "] for user [" + authenticatedUser.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.deleteItem(authenticatedUser.getId(), listId, listItemId);

            return createAndLogResponse(NO_CONTENT);
        } catch (ShoppingListNotFoundException | ShoppingListItemNotFoundException e) {
            LOGGER.error("Error while deleting item [" + listItemId + "] in shopping list [" + listId + "] for user [" + authenticatedUser.getId() + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    @Path("{shoppingListId}/cart")
    @RolesAllowed({USER, ADMIN})
    @DELETE
    public Response emptyCart(
            @Auth User authenticatedUser,
            @PathParam(SHOPPING_LIST_ID) String shoppingListIdString
    ) {
        LOGGER.info("Received request to empty cart of shopping list [{}] for user [{}]", shoppingListIdString, authenticatedUser.getId());

        ShoppingListId listId;
        try {
            listId = ShoppingListId.parse(shoppingListIdString);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error while emptying cart of shopping list [" + shoppingListIdString + "]", e);
            return Response.status(BAD_REQUEST).build();
        }

        try {
            shoppingListApplication.emptyCart(authenticatedUser.getId(), listId);

            return createAndLogResponse(NO_CONTENT);
        } catch (ShoppingListNotFoundException e) {
            LOGGER.error("Error while emptying cart of shopping list [" + listId + "]", e);
            return Response.status(BAD_REQUEST).build();
        }
    }

    private Response createAndLogResponse(Response.Status status) {
        return createAndLogResponse(status, null);
    }

    private Response createAndLogResponse(Response.Status status, Object body) {
        LOGGER.info("Responding to request with status [{}] and body [{}]", status, body);
        return Response.status(status).entity(body).build();
    }
}
