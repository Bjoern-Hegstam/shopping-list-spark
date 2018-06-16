package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.persistence.JdbiUserRepository;
import com.bhegstam.shoppinglist.port.rest.UserBean;
import com.bhegstam.util.TestDatabaseSetup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.Matchers.is;

public class UserApplicationIntegrationTest {
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private UserApplication userApplication;
    private UserId userId;

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    @Before
    public void setUp() {
        userApplication = new UserApplication(testDatabaseSetup.getJdbi().onDemand(JdbiUserRepository.class));
        userId = userApplication.addUser("Foo", "Bar", "foo@bar.com");
    }

    @Test
    public void updateUser_emptyBean() {
        // given
        UserBean userBean = new UserBean();

        // when
        userApplication.updateUser(userId, userBean);

        // then
        User user = userApplication.getUser(userId);

        errorCollector.checkThat(user.getRole(), is(Role.USER));
        errorCollector.checkThat(user.isVerified(), is(false));
    }

    @Test
    public void updateUser_setVerified() {
        // given
        UserBean userBean = new UserBean();
        userBean.setVerified(true);

        // when
        userApplication.updateUser(userId, userBean);

        // then
        User user = userApplication.getUser(userId);

        errorCollector.checkThat(user.getRole(), is(Role.USER));
        errorCollector.checkThat(user.isVerified(), is(true));
    }

    @Test
    public void updateUser_makeAdmin() {
        // given
        UserBean userBean = new UserBean();
        userBean.setRole(Role.ADMIN);

        // when
        userApplication.updateUser(userId, userBean);

        // then
        User user = userApplication.getUser(userId);

        errorCollector.checkThat(user.getRole(), is(Role.ADMIN));
        errorCollector.checkThat(user.isVerified(), is(false));
    }
}