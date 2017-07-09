package com.bhe.user;

import com.bhe.util.Filters;
import com.bhe.util.Path;
import com.bhe.util.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;

import static com.bhe.util.webapp.ResultBuilder.result;
import static com.bhe.util.webapp.SparkWrappers.asSparkRoute;

public class UserAdministrationController {
    private final UserRepository userRepository;

    @Inject
    public UserAdministrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void configureRoutes(Service http) {
        http.path(Path.Web.ADMIN, () -> {
            http.before("/*", Filters::userIsAdmin);
            http.get(Path.Web.USERS, asSparkRoute(request -> serverUserList()));
        });
    }

    private Result serverUserList() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("users", userRepository.getUsers());

        return result().render(Path.Template.ADMIN_USERS, model);
    }
}
