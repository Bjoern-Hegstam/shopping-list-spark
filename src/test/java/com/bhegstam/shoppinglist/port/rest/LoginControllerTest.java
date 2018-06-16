package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.shoppinglist.persistence.JdbiUserRepository;
import com.bhegstam.util.Mocks;
import com.bhegstam.util.TestDatabaseSetup;
import com.bhegstam.webutil.JsonResponseTransformer;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.bhegstam.util.Matchers.isPresentAnd;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginControllerTest {
    private static final String USERNAME = "John";
    private static final String PASSWORD = "jp93";
    private static final String EMAIL = "john@domain.com";
    private static final String WRONG_USERNAME = "Peter";
    private static final String WRONG_PASSWORD = "abc";

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    private UserRepository userRepository;
    private LoginController loginController;

    @Before
    public void setUp() {
        userRepository = testDatabaseSetup.getJdbi().onDemand(JdbiUserRepository.class);
        loginController = new LoginController(new UserApplication(userRepository));
    }

    @Test
    public void login() {
        // given
        UserId userId = userRepository.create(new User(USERNAME, PASSWORD, EMAIL, true));
        User user = userRepository.get(userId);

        Request request = mockLoginRequest(USERNAME, PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertThat(result.getStatusCode(), isPresentAnd(is(HttpStatus.OK_200)));
        assertUserLoggedIn(request, user);
    }

    @Test
    public void login_usernameNotRecognized() {
        // given
        userRepository.create(new User(USERNAME, PASSWORD, EMAIL, true));

        Request request = mockLoginRequest(WRONG_USERNAME, PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertThat(result.getStatusCode(), isPresentAnd(is(HttpStatus.UNAUTHORIZED_401)));
        assertUserNotLoggedIn(request);
    }

    @Test
    public void login_invalidPassword() {
        // given
        userRepository.create(new User(USERNAME, PASSWORD, EMAIL, true));

        Request request = mockLoginRequest(USERNAME, WRONG_PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertThat(result.getStatusCode(), isPresentAnd(is(HttpStatus.UNAUTHORIZED_401)));
        assertUserNotLoggedIn(request);
    }

    @Test
    public void login_userNotVerified() {
        // given
        userRepository.create(new User(USERNAME, PASSWORD, EMAIL, false));

        Request request = mockLoginRequest(USERNAME, PASSWORD);

        // when
        Result result = loginController.handleLoginPost(request);

        // then
        assertThat(result.getStatusCode(), isPresentAnd(is(HttpStatus.UNAUTHORIZED_401)));
        assertUserNotLoggedIn(request);
    }

    @Test
    public void logout() {
        // given
        Request request = Mocks.mockRequest();
        // when
        Result result = loginController.handleLogoutPost(request);

        // then
        verify(request.session()).unsetCurrentUser();
        assertThat(result.getStatusCode(), isPresentAnd(is(HttpStatus.OK_200)));
    }

    private Request mockLoginRequest(String username, String password) {
        Request request = Mocks.mockRequest();
        try {
            when(request.body()).thenReturn(new JsonResponseTransformer().render(new LoginDto(username, password)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return request;
    }

    private void assertUserLoggedIn(Request request, User user) {
        verify(request.session()).setCurrentUser(user);
    }

    private void assertUserNotLoggedIn(Request request) {
        verify(request.session(), never()).setCurrentUser(any());
    }
}