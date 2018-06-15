package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.io.IOException;
import java.util.Optional;

import static com.bhegstam.shoppinglist.port.rest.ContentType.APPLICATION_JSON;
import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;


public class LoginController implements Controller {
    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final UserRepository userRepository;

    @Inject
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        JsonResponseTransformer jsonResponseTransformer = new JsonResponseTransformer();

        http.post(Path.Web.LOGIN, asSparkRoute(this::handleLoginPost), jsonResponseTransformer);
        http.post(Path.Web.LOGOUT, asSparkRoute(this::handleLogoutPost), jsonResponseTransformer);
    }

    Result handleLoginPost(Request request) {
        LoginDto loginDto;
        try {
            loginDto = OBJECT_MAPPER.readValue(request.body(), LoginDto.class);
        } catch (IOException e) {
            LOGGER.error("Unable to parse body for login request");
            return result()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY_422)
                    .returnPayload(null);
        }

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        Optional<User> user = userRepository
                .findByUsername(username)
                .filter(u -> u.hasPassword(password));

        if (!user.isPresent()) {
            request.session().setErrorMessage(Message.LOGIN_AUTH_FAILED);
            return result()
                    .statusCode(HttpStatus.UNAUTHORIZED_401)
                    .returnPayload(null);
        }

        if (!user.get().isVerified()) {
            request.session().setErrorMessage(Message.LOGIN_USER_PENDING_VERIFICATION);
            return result()
                    .statusCode(HttpStatus.UNAUTHORIZED_401)
                    .returnPayload(null);
        }

        request.session().setCurrentUser(user.get());
        LOGGER.debug("User {} has logged in", user.get().getId());

        return result()
                .statusCode(HttpStatus.OK_200)
                .type(APPLICATION_JSON)
                .returnPayload(UserBean.fromUser(user.get()));
    }

    Result handleLogoutPost(Request request) {
        LOGGER.debug("Logging out");
        request.session().unsetCurrentUser();
        return result()
                .statusCode(HttpStatus.OK_200)
                .type(APPLICATION_JSON)
                .returnPayload(null);
    }
}
