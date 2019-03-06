package com.bhegstam.shoppinglist.port.rest.auth;

import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.port.rest.TokenGenerator;
import io.dropwizard.auth.Auth;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;
import static com.bhegstam.shoppinglist.port.rest.auth.RestApiMimeType.AUTH_1_0;
import static javax.ws.rs.core.Response.Status.OK;


@Path("auth")
@Produces(AUTH_1_0)
public class AuthResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
    private final byte[] tokenSecret;

    public AuthResource(byte[] tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @RolesAllowed({USER, ADMIN})
    @POST
    public Response generateTokenAndGetUser(@Auth User user) {
        LOGGER.debug("Received request to generate token for user [{}]", user.getId());

        String token;
        try {
            token = TokenGenerator.generate(user, tokenSecret);
        } catch (JoseException e) {
            LOGGER.error("Error when returning token for user [" + user.getUsername() + "]", e);
            return Response.serverError().build();
        }
        return Response
                .status(OK)
                .entity(new AuthResponse(token, user))
                .build();
    }

    @Path("ping")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response ping(@Auth User user) {
        LOGGER.info("Received ping from user [{}]", user.getId());
        return Response
                .status(OK)
                .build();
    }
}
