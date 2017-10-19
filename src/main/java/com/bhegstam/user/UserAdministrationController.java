package com.bhegstam.user;

import com.bhegstam.webutil.Filters;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Result;
import com.bhegstam.util.Path;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;

public class UserAdministrationController implements Controller {
    private final UserRepository userRepository;

    @Inject
    public UserAdministrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
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
