package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.*;
import com.bhegstam.shoppinglist.util.TestDatabaseSetup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserShoppingListApplicationIntegrationTest {
    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private UserApplication userApplication;
    private UserId userId;

    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    private final WorkspaceRepository workspaceRepository = testDatabaseSetup.getRepositoryFactory().createWorkspaceRepository();

    @Before
    public void setUp() {
        userApplication = new UserApplication(
                testDatabaseSetup.getRepositoryFactory().createUserRepository(),
                workspaceRepository
        );
        userId = userApplication.addUser("Foo", "Bar", "foo@bar.com");
    }

    @Test
    public void verifyAddedUser() {
        User user = userApplication.getUser(userId);

        errorCollector.checkThat(user.getUsername(), is("Foo"));
        errorCollector.checkThat(user.getEmail(), is("foo@bar.com"));
        errorCollector.checkThat(user.getRole(), is(Role.USER));
        errorCollector.checkThat(user.isVerified(), is(false));
    }

    @Test
    public void verifyDefaultWorkspace() {
        WorkspaceRepository workspaceRepository = this.workspaceRepository;

        Workspace workspace = workspaceRepository.getDefaultWorkspace(userId);

        errorCollector.checkThat(workspace, notNullValue());
    }

    @Test
    public void updateUser_setVerified() {
        // when
        userApplication.updateUser(userId, Role.USER, true);

        // then
        User user = userApplication.getUser(userId);

        errorCollector.checkThat(user.getRole(), is(Role.USER));
        errorCollector.checkThat(user.isVerified(), is(true));
    }

    @Test
    public void updateUser_makeAdmin() {
        // when
        userApplication.updateUser(userId, Role.ADMIN, false);

        // then
        User user = userApplication.getUser(userId);

        errorCollector.checkThat(user.getRole(), is(Role.ADMIN));
        errorCollector.checkThat(user.isVerified(), is(false));
    }
}
