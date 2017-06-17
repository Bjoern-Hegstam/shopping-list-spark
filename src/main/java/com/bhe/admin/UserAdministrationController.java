package com.bhe.admin;

import com.bhe.user.UserRepository;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;

import java.util.HashMap;

import static com.bhe.util.webapp.ResultBuilder.result;

public class UserAdministrationController {
    private final UserRepository userRepository;

    public UserAdministrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result serverUserList(Request request) {
        if (!request.session().isUserLoggedIn()) {
            return result().redirectTo(Path.Web.LOGIN);
        }

        HashMap<String, Object> model = new HashMap<>();
        model.put("users", userRepository.getUsers());

        return result().render(Path.Template.ADMIN_USERS, model);
    }
}
