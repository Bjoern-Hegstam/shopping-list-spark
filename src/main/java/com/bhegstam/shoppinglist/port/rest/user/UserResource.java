package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.PATCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.port.rest.user.RestApiMimeType.USER_1_0;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path("user")
@Produces(USER_1_0)
public class UserResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    private final UserApplication userApplication;

    public UserResource(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @POST
    public Response createUser(@Auth User user, @Valid CreateUserRequest request) {
        LOGGER.info("Received request to create user [{}] by user [{}]", request.getUsername(), user.getId());

        UserId userId = userApplication.addUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );

        return createAndLogResponse(CREATED, new UserCreatedResponse(userId));
    }

    @RolesAllowed(ADMIN)
    @GET
    public Response getUsers(@Auth User user) {
        LOGGER.info("Received request to get users for user [{}]", user.getId());

        List<User> users = userApplication.getUsers();

        return createAndLogResponse(OK, new GetUsersResponse(users));
    }

    @Path("/{userId}")
    @RolesAllowed(ADMIN)
    @PATCH // TODO: Replace with PUT
    public Response patchUser(@Auth User user, @PathParam("userId") String userIdString, @Valid UpdateUserRequest request) {
        LOGGER.info("Received request [{}] to update user [{}] for user [{}]", request, userIdString, user);

        UserId userId = UserId.from(userIdString);

        User updateUser = userApplication.updateUser(userId, Role.fromString(request.getRole()), request.getVerified());

        return createAndLogResponse(OK, new UserResponse(updateUser));
    }

    private Response createAndLogResponse(Response.Status status, Object body) {
        LOGGER.info("Responding to request with status [{}] and body [{}]", status, body);
        return Response.status(status).entity(body).build();
    }
}
