package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.persistence.JdbiUserRepository;
import com.bhegstam.util.TestDatabaseSetup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static com.bhegstam.util.Matchers.isPresent;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class UserApplicationIntegrationTest {
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
    public void getById() {
        // when
        User user = userApplication.getUser(userId);

        // then
        assertThat(user.getId(), is(userId));
        assertThat(user.getUsername(), is("Foo"));
    }

    @Test
    public void findByUsername_unknownUser() {
        // when
        Optional<User> user = userApplication.findByUsername("unknown");

        // then
        assertThat(user, not(isPresent()));
    }

    @Test
    public void findByUsername() {
        // when
        User user = userApplication.findByUsername("Foo").get();

        // then
        assertThat(user.getId(), is(userId));
    }
}