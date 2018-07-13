package com.bhegstam.shoppinglist.port.rest.auth;

import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.Auth;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;


@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
    private final byte[] tokenSecret;

    public AuthResource(byte[] tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Path("/")
    @RolesAllowed({USER, ADMIN})
    @POST
    public Response generateTokenAndGetUser(@Auth User user) {
        LOGGER.debug("Generating token for user {}", user.getId());

        JwtClaims claims = new JwtClaims();
        claims.setSubject(user.getUsername());
        claims.setExpirationTimeMinutesInTheFuture(60);
        claims.setIssuedAtToNow();

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(HMAC_SHA256);
        jws.setKey(new HmacKey(tokenSecret));

        try {
            return Response
                    .ok()
                    .entity(new AuthResponse(jws.getCompactSerialization(), user))
                    .build();
        } catch (JoseException e) {
            LOGGER.error("Error when returning token for user " + user.getUsername(), e);
            return Response.serverError().build();
        }
    }

    @Path("ping")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response ping(@Auth User user) {
        LOGGER.debug("Response to ping from user {}", user.getId());
        return Response.ok().build();
    }
}
