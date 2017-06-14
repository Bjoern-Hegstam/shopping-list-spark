package com.bhe.login;

import com.bhe.user.User;
import com.bhe.user.UserRepositoryInMem;
import com.bhe.util.Message;
import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import org.junit.Before;
import org.junit.Test;

import static com.bhe.util.Mocks.mockRequest;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    private UserRepositoryInMem userRepository;
    private LoginController loginController;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepositoryInMem();
        loginController = new LoginController(userRepository);
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

    @Test
    public void login_normalCase() {
        // given
        User user = new User(
                "John",
                "jp93",
                "john@domain.com",
                true
        );
        userRepository.create(user);

        Request request = mockRequest();
        when(request.queryParams(LoginParameter.USERNAME)).thenReturn("John");
        when(request.queryParams(LoginParameter.PASSWORD)).thenReturn("jp93");

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertEquals(Path.Web.INDEX, result.redirectPath);
        assertUserLoggedIn(request, user);
    }

    private void assertUserLoggedIn(Request request, User user) {
        verify(request.session()).setCurrentUser(user);
    }

    @Test
    public void login_whenUserNotVerified_shouldShowErrorMessage() {
        // given
        User unverifiedUser = new User(
                "John",
                "jp93",
                "john@domain.com",
                false
        );
        userRepository.create(unverifiedUser);

        Request request = mockRequest();
        when(request.queryParams(LoginParameter.USERNAME)).thenReturn("John");
        when(request.queryParams(LoginParameter.PASSWORD)).thenReturn("jp93");

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertEquals(Path.Template.LOGIN, result.renderTemplatePath);
        verify(request.session()).setErrorMessage(Message.LOGIN_USER_PENDING_VERIFICATION);
        assertUserNotLoggedIn(request);
    }

    private void assertUserNotLoggedIn(Request request) {
        verify(request.session(), never()).setCurrentUser(any());
    }
}