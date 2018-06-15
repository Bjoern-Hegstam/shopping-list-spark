package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.webutil.Filters;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import spark.Service;

import java.util.Optional;

import static com.bhegstam.shoppinglist.port.rest.ContentType.APPLICATION_JSON;
import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class UserApiController implements Controller {
    private final UserRepository userRepository;

    @Inject
    public UserApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.path(Path.Api.USER, () -> {
            http.before("/*", Filters.userIsAdmin(Filters.Actions.redirectNotAuthorized(Path.Web.INDEX)));
            http.patch(
                    "/:userId",
                    APPLICATION_JSON,
                    asSparkRoute(this::patchUser),
                    new JsonResponseTransformer()
            );
        });
    }

    private Result patchUser(Request request) {
        User user = userRepository.get(UserId.from(request.params("userId")));

        UserBean userBean = UserBean.fromJson(request.body());
        Optional.ofNullable(userBean.getRole()).ifPresent(user::setRole);
        Optional.ofNullable(userBean.getVerified()).ifPresent(user::setVerified);

        userRepository.update(user);

        return result()
                .statusCode(HttpStatus.OK_200)
                .type(APPLICATION_JSON)
                .returnPayload(UserBean.fromUser(user));
    }
}
