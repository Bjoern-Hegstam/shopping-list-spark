package com.bhegstam.shoppinglist.port.rest.login;

import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.webutil.webapp.Result;
import io.dropwizard.auth.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;


@Path("api")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
    private static final Logger LOGGER = LogManager.getLogger(LoginResource.class);

    @Path("login")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Result login(@Auth User user) {
        LOGGER.debug("Generating token for user {}", user.getId());
        // TODO: Generate and return JWT token
        return null;
    }
}
