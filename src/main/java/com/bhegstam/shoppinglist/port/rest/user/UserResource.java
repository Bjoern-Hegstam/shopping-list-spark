package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.Workspace;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;
import static com.bhegstam.shoppinglist.port.rest.user.RestApiMimeType.USER_1_0;
import static javax.ws.rs.core.Response.Status.OK;

@Path("user")
@Produces(USER_1_0)
public class UserResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    private final UserApplication userApplication;

    public UserResource(UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getAuthenticatedUser(@Auth User authenticatedUser) {
        LOGGER.info("Received request to get self for user [{}]", authenticatedUser.getId());

        return createAndLogResponse(OK, new UserResponse(authenticatedUser));
    }

    @Path("workspace")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response getWorkspaces(@Auth User authenticatedUser) {
        LOGGER.info("Received request to get workspaces for user [{}]", authenticatedUser.getId());

        List<Workspace> workspaces = userApplication.getWorkspaces(authenticatedUser.getId());

        return createAndLogResponse(OK, new GetWorkspacesResponse(workspaces));
    }

    private Response createAndLogResponse(Response.Status status, Object body) {
        return createAndLogResponse(status.getStatusCode(), body);
    }

    private Response createAndLogResponse(int statusCode, Object body) {
        LOGGER.info("Responding to request with status [{}] and body [{}]", statusCode, body);
        return Response.status(statusCode).entity(body).build();
    }
}
