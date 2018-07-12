package com.bhegstam.shoppinglist.port.rest.login;

import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.webutil.webapp.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("api")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {
    private static final Logger LOGGER = LogManager.getLogger(LoginResource.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Path("login")
    @POST
    public Result login(@Auth User user) {
        LOGGER.debug("Generating token for user {}", user.getId());
        // TODO: Generate and return JWT token
        return null;
    }
}
