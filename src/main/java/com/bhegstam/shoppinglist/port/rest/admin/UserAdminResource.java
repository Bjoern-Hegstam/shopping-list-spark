package com.bhegstam.shoppinglist.port.rest.admin;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.port.rest.user.UserResource;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.port.rest.admin.RestApiMimeType.USER_ADMIN_1_0;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path("admin/user")
@Produces(USER_ADMIN_1_0)
public class UserAdminResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    private final UserApplication userApplication;

    public UserAdminResource(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @RolesAllowed(ADMIN)
    @POST
    public Response createUser(@Auth User authenticatedUser, @Valid CreateUserRequest request) {
        LOGGER.info("Received request to create user [{}] by user [{}]", request.getUsername(), authenticatedUser.getId());

        UserId userId;
        try {
            userId = userApplication.createUser(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail()
            );
        } catch (InvalidUsernameException e) {
            return createAndLogResponse(422, ErrorResponse.exception(e));
        } catch (InvalidEmailException e) {
            return createAndLogResponse(422, ErrorResponse.exception(e));
        }

        return createAndLogResponse(CREATED, new UserCreatedResponse(userId));
    }

    @Path("{userId}")
    @RolesAllowed(ADMIN)
    @GET
    public Response getUser(@Auth User authenticatedUser, @PathParam("userId") String userIdString) {
        LOGGER.info("Received request to get user with id [{}] for user [{}]", userIdString, authenticatedUser);

        UserId userId = UserId.from(userIdString);
        User user = userApplication.getUser(userId);

        return createAndLogResponse(OK, new UserResponse(user));
    }

    @Path("{userId}")
    @RolesAllowed(ADMIN)
    @PUT
    public Response updateUser(@Auth User authenticatedUser, @PathParam("userId") String userIdString, @Valid UpdateUserRequest request) {
        LOGGER.info("Received request [{}] to update user [{}] for user [{}]", request, userIdString, authenticatedUser);

        UserId userId = UserId.from(userIdString);

        User updateUser = userApplication.updateUser(userId, Role.fromString(request.getRole()), request.isVerified());

        return createAndLogResponse(OK, new UserResponse(updateUser));
    }

    private Response createAndLogResponse(Response.Status status, Object body) {
        return createAndLogResponse(status.getStatusCode(), body);
    }

    private Response createAndLogResponse(int statusCode, Object body) {
        LOGGER.info("Responding to request with status [{}] and body [{}]", statusCode, body);
        return Response.status(statusCode).entity(body).build();
    }
}
