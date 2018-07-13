package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.UserRegistration;
import com.bhegstam.shoppinglist.util.Mocks;
import com.bhegstam.webutil.webapp.Request;
import com.bhegstam.webutil.webapp.Result;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserRegistrationControllerTest {

    private UserRegistrationController userRegistrationController;
    private UserRegistration userRegistration;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        userRegistration = mock(UserRegistration.class);
        userRegistrationController = new UserRegistrationController(userRegistration);
    }

    @Test
    public void serveRegistrationPage() {
        // when
        Result result = userRegistrationController.serveRegistrationPage(mock(Request.class));

        // then
        errorCollector.checkThat(result.getRenderTemplatePath(), is(Path.Template.REGISTER));
    }

    @Test
    public void registerNewUser_success() {
        // given
        Request request = mockRegistrationRequest();
        userRegistrationAllowed(true);

        // when
        Result result = userRegistrationController.registerNewUser(request);

        // then
        errorCollector.checkThat(result.getRedirectPath(), is(Path.Web.LOGIN));
    }

    @Test
    public void registerNewUser_failure() {
        // given
        Request request = mockRegistrationRequest();
        userRegistrationAllowed(false);

        // when
        Result result = userRegistrationController.registerNewUser(request);

        // then
        errorCollector.checkThat(result.getRenderTemplatePath(), is(Path.Template.REGISTER));
        verify(request.session()).setErrorMessage(Message.USER_REGISTRATION_FAILED);
    }

    private Request mockRegistrationRequest() {
        Request request = Mocks.mockRequest();
        when(request.queryParams("username")).thenReturn("user");
        when(request.queryParams("password")).thenReturn("password");
        when(request.queryParams("email")).thenReturn("email");
        return request;
    }

    private void userRegistrationAllowed(boolean allowed) {
        when(userRegistration.register(any())).thenReturn(allowed);
    }
}