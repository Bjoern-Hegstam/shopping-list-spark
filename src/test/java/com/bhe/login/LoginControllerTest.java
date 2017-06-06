package com.bhe.login;

import com.bhe.user.UserRepositoryInMem;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import com.bhe.util.webapp.Session;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
        Request request = mockRequestWithSession();
        when(request.session().isUserLoggedIn()).thenReturn(true);

        // when
        Result result = loginController.serveLoginPage(request);

        // then
        assertEquals(Path.Web.INDEX, result.redirectPath);
    }

    @Test
    public void serveLoginPage_whenNotLoggedIn_shouldRenderLogin() {
        // given
        Request request = mockRequestWithSession();
        when(request.session().isUserLoggedIn()).thenReturn(false);

        // when
        Result result = loginController.serveLoginPage(request);

        // then
        assertEquals(Path.Template.LOGIN, result.renderTemplatePath);
    }

    private Request mockRequestWithSession() {
        Request request = mock(Request.class);
        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        return request;
    }
}