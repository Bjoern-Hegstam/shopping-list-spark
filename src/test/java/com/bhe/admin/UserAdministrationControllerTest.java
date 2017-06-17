package com.bhe.admin;

import com.bhe.user.UserRepository;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import org.junit.Before;
import org.junit.Test;

import static com.bhe.util.Mocks.mockRequest;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAdministrationControllerTest {

    private UserAdministrationController controller;

    @Before
    public void setUp() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        controller = new UserAdministrationController(userRepository);
    }

    @Test
    public void serveUserList_whenUserNotLoggedIn() {
        // given
        Request request = mockRequest();
        when(request.session().isUserLoggedIn()).thenReturn(false);

        // when
        Result result = controller.serverUserList(request);

        // then
        assertEquals(Path.Web.LOGIN, result.redirectPath);
    }
}