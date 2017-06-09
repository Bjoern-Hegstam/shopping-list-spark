package com.bhe.user;

import com.bhe.util.Path;
import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Result;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static com.bhe.util.Mocks.mockRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserRegistration userRegistration;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() throws Exception {
        userRegistration = mock(UserRegistration.class);
        userController = new UserController(userRegistration);
    }

    @Test
    public void serveRegistrationPage() {
        // when
        Result result = userController.serveRegistrationPage(mock(Request.class));

        // then
        errorCollector.checkThat(result.renderTemplatePath, is(Path.Template.REGISTER));
    }

    @Test
    public void registerNewUser_success() {
        // given
        Request request = mockRegistrationRequest();
        userRegistrationAllowed(true);

        // when
        Result result = userController.registerNewUser(request);

        // then
        errorCollector.checkThat(result.redirectPath, is(Path.Web.LOGIN));
    }

    @Test
    public void registerNewUser_failure() {
        // given
        Request request = mockRegistrationRequest();
        userRegistrationAllowed(false);

        // when
        Result result = userController.registerNewUser(request);

        // then
        errorCollector.checkThat(result.renderTemplatePath, is(Path.Template.REGISTER));
        verify(request.session()).setErrorMessage("USER_REGISTER_NOT_ALLOWED");
    }

    private Request mockRegistrationRequest() {
        Request request = mockRequest();
        when(request.queryParams("username")).thenReturn("user");
        when(request.queryParams("password")).thenReturn("password");
        when(request.queryParams("email")).thenReturn("email");
        return request;
    }

    private void userRegistrationAllowed(boolean allowed) {
        when(userRegistration.registerNewUser(any(), any(), any())).thenReturn(allowed);
    }
}