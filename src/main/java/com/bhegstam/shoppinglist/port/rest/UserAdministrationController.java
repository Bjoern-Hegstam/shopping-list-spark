package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.webutil.webapp.Controller;
import com.bhegstam.webutil.webapp.Result;
import com.google.inject.Inject;
import spark.Service;

import java.util.HashMap;
import java.util.Map;

import static com.bhegstam.webutil.webapp.ResultBuilder.result;
import static com.bhegstam.webutil.webapp.SparkWrappers.asSparkRoute;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class UserAdministrationController implements Controller {
    private final UserRepository userRepository;

    @Inject
    public UserAdministrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void configureRoutes(Service http) {
        http.get(
                Path.Web.ADMIN + Path.Web.USERS,
                asSparkRoute(request -> serveUserList())
        );
    }

    private Result serveUserList() {
        Map<String, Object> model = new HashMap<>();
        model.put(
                "users",
                userRepository
                        .getUsers().stream()
                        .map(UserBean::fromUser)
                        .sorted(comparing(UserBean::getUsername))
                        .collect(toList())
        );

        return result().render(Path.Template.ADMIN_USERS, model);
    }
}
