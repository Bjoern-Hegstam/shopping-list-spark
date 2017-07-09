package com.bhe.user.api;

import com.bhe.user.User;
import com.bhe.user.UserRepository;
import com.bhe.util.Filters;
import com.bhe.util.JsonResponseTransformer;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.Optional;

import static com.bhe.util.webapp.ResultBuilder.result;
import static com.bhe.util.webapp.SparkWrappers.asSparkRoute;

public class UserApiController {
    private final UserRepository userRepository;

    @Inject
    public UserApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        String userId = request.params("userId");
        UserBean userBean = UserBean.fromJson(request.body());

        User user = userRepository.get(Integer.parseInt(userId));

        Optional.ofNullable(userBean.getRole()).ifPresent(user::setRole);
        Optional.ofNullable(userBean.getVerified()).ifPresent(user::setVerified);

        userRepository.update(user);

        return result().returnPayload(UserBean.fromUser(user));
    }
}
