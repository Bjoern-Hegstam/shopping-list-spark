package com.bhe.user.api;

import com.bhe.user.User;
import com.bhe.user.UserId;
import com.bhe.user.UserRepository;
import com.bhe.util.Path;
import com.github.bhe.webutil.Filters;
import com.github.bhe.webutil.JsonResponseTransformer;
import com.github.bhe.webutil.webapp.Controller;
import com.github.bhe.webutil.webapp.Request;
import com.github.bhe.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.Optional;

import static com.github.bhe.webutil.webapp.ResultBuilder.result;
import static com.github.bhe.webutil.webapp.SparkWrappers.asSparkRoute;

public class UserApiController implements Controller {
    private final UserRepository userRepository;

    @Inject
    public UserApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.path(Path.Api.USER, () -> {
            http.before("/*", Filters::userIsAdmin);
            http.patch(
                    "/:userId",
                    "application/json",
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

        return result().returnPayload(UserBean.fromUser(user));
    }
}
