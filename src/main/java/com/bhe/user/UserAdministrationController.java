package com.bhe.user;

import com.bhe.util.Path;
import com.github.bhe.webutil.Filters;
import com.github.bhe.webutil.webapp.Controller;
import com.github.bhe.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;

import static com.github.bhe.webutil.webapp.ResultBuilder.result;
import static com.github.bhe.webutil.webapp.SparkWrappers.asSparkRoute;

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
