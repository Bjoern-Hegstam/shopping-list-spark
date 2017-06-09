package com.bhe.login;

import com.bhe.user.UserRepositoryInMem;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import org.junit.Before;
import org.junit.Test;

import static com.bhe.util.Mocks.mockRequest;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    private LoginController loginController;

    @Before
    public void setUp() throws Exception {
        loginController = new LoginController(new UserRepositoryInMem());
    }

    @Test
    public void serveLoginPage_whenLoggedIn_shouldRedirectToIndex() {
        // given
        Request request = mockRequest();
        when(request.session().isUserLoggedIn()).thenReturn(true);

        // when
        Result result = loginController.serveLoginPage(request);

        // then
        assertEquals(Path.Web.INDEX, result.redirectPath);
    }

    @Test
    public void serveLoginPage_whenNotLoggedIn_shouldRenderLogin() {
        // given
        Request request = mockRequest();
        when(request.session().isUserLoggedIn()).thenReturn(false);

        // when
        Result result = loginController.serveLoginPage(request);

        // then
        assertEquals(Path.Template.LOGIN, result.renderTemplatePath);
    }
}