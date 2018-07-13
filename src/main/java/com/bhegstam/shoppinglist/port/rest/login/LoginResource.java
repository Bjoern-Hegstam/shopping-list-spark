package com.bhegstam.shoppinglist.port.rest.login;

import com.bhegstam.shoppinglist.domain.User;
import io.dropwizard.auth.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.bhegstam.shoppinglist.domain.Role.RoleName.ADMIN;
import static com.bhegstam.shoppinglist.domain.Role.RoleName.USER;
import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;


@Path("api")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
    private static final Logger LOGGER = LogManager.getLogger(LoginResource.class);
    private final byte[] tokenSecret;

    public LoginResource(byte[] tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Path("login")
    @RolesAllowed({USER, ADMIN})
    @GET
    public Response login(@Auth User user) {
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
                    .entity(new LoginResponse(jws.getCompactSerialization()))
                    .build();
        } catch (JoseException e) {
            LOGGER.error("Error when returning token for user " + user.getUsername(), e);
            return Response.serverError().build();
        }
    }
}
