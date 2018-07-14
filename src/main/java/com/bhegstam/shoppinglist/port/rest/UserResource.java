package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.PATCH;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserApplication userApplication;

    public UserResource(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @RolesAllowed(ADMIN)
    @POST
    public Response createUser(@Auth User user, @Valid CreateUserRequest request) {
        UserId userId = userApplication.addUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );

        return Response
                .status(CREATED)
                .entity(new UserCreatedResponse(userId))
                .build();
    }

    @RolesAllowed(ADMIN)
    @GET
    public Response getUsers(@Auth User user) {
        List<User> users = userApplication.getUsers();

        return Response
                .status(OK)
                .entity(new GetUsersResponse(users))
                .build();
    }

    @Path("/{userId}")
    @RolesAllowed(ADMIN)
    @PATCH // TODO: Replace with PUT
    public Response patchUser(@Auth User user, @PathParam("userId") String userIdString, @Valid UpdateUserRequest request) {
        UserId userId = UserId.from(userIdString);

        User updateUser = userApplication.updateUser(userId, Role.fromString(request.getRole()), request.getVerified());

        return Response
                .status(OK)
                .entity(new UserResponse(updateUser))
                .build();
    }
}
