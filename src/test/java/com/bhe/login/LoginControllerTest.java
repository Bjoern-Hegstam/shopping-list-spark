package com.bhe.login;

import com.bhe.user.User;
import com.bhe.user.UserId;
import com.bhe.user.UserRepositoryInMem;
import com.bhe.util.Message;
import com.bhe.util.Path;
import com.github.bhe.webutil.webapp.Request;
import com.github.bhe.webutil.webapp.Result;
import org.junit.Before;
import org.junit.Test;

import static com.bhe.util.Mocks.mockRequest;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginControllerTest {
    private static final String USERNAME = "John";
    private static final String PASSWORD = "jp93";
    private static final String EMAIL = "john@domain.com";
    private static final String WRONG_USERNAME = "Peter";
    private static final String WRONG_PASSWORD = "abc";

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
        UserId userId = userRepository.create(new User(USERNAME, PASSWORD, EMAIL, true));
        User user = userRepository.get(userId);

        Request request = mockLoginRequest(USERNAME, PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertEquals(Path.Web.INDEX, result.redirectPath);
        assertUserLoggedIn(request, user);
    }

    @Test
    public void login_whenUsernameNotRecognized() {
        // given
        userRepository.create(new User(USERNAME, PASSWORD, EMAIL, true));

        Request request = mockLoginRequest(WRONG_USERNAME, PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertEquals(Path.Template.LOGIN, result.renderTemplatePath);
        verify(request.session()).setErrorMessage(Message.LOGIN_AUTH_FAILED);
        assertUserNotLoggedIn(request);
    }

    @Test
    public void login_whenPasswordInvalid() {
        // given
        userRepository.create(new User(USERNAME, PASSWORD, EMAIL, true));

        Request request = mockLoginRequest(USERNAME, WRONG_PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertEquals(Path.Template.LOGIN, result.renderTemplatePath);
        verify(request.session()).setErrorMessage(Message.LOGIN_AUTH_FAILED);
        assertUserNotLoggedIn(request);
    }

    @Test
    public void login_whenUserNotVerified_shouldShowErrorMessage() {
        // given
        userRepository.create(new User(USERNAME, PASSWORD, EMAIL, false));

        Request request = mockLoginRequest(USERNAME, PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertEquals(Path.Template.LOGIN, result.renderTemplatePath);
        verify(request.session()).setErrorMessage(Message.LOGIN_USER_PENDING_VERIFICATION);
        assertUserNotLoggedIn(request);
    }

    @Test
    public void logout() {
        // given
        Request request = mockRequest();
        // when
        Result result = loginController.handleLogoutPost(request);

        // then
        verify(request.session()).unsetCurrentUser();
        assertEquals(Path.Web.LOGIN, result.redirectPath);
    }

    private Request mockLoginRequest(String username, String password) {
        Request request = mockRequest();
        when(request.queryParams(LoginParameter.USERNAME)).thenReturn(username);
        when(request.queryParams(LoginParameter.PASSWORD)).thenReturn(password);
        return request;
    }

    private void assertUserLoggedIn(Request request, User user) {
        verify(request.session()).setCurrentUser(user);
    }

    private void assertUserNotLoggedIn(Request request) {
        verify(request.session(), never()).setCurrentUser(any());
    }
}