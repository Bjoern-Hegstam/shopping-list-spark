package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.bhegstam.util.TestDatabaseSetup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.List;
import java.util.Optional;

import static com.bhegstam.util.Matchers.isPresent;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class JdbiUserRepositoryTest {
    @Rule
    public TestDatabaseSetup testDatabaseSetup = new TestDatabaseSetup();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private UserRepository userRepository;
    private UserId userId;
    private UserId adminId;
    private User unpersistedUser;
    private User unpersistedAdmin;

    @Before
    public void setUp() {
        userRepository = testDatabaseSetup.getRepositoryFactory().createUserRepository();

        unpersistedUser = new User("Foo", "bar", "foo@bar.com");
        userId = userRepository.create(unpersistedUser);

        unpersistedAdmin = new User("FooAdmin", "bar", "foo@admin.com", true, Role.ADMIN);
        adminId = userRepository.create(unpersistedAdmin);
    }

    @Test
    public void get_unverifiedUser() {
        User persistedUser = userRepository.get(userId);

        validateUser(persistedUser, userId, unpersistedUser);
    }

    @Test
    public void get_verifiedAdmin() {
        User persistedAdmin = userRepository.get(adminId);

        validateUser(persistedAdmin, adminId, unpersistedAdmin);
    }

    @Test
    public void updateUser() {
        // given
        User user = userRepository.get(userId);
        user.setRole(Role.ADMIN);
        user.setVerified(true);

        // when
        userRepository.update(user);

        // then
        User updatedUser = userRepository.get(userId);
        validateUser(updatedUser, userId, user);
    }

    @Test
    public void findByUsername_unknownUser() {
        Optional<User> user = userRepository.findByUsername("unknown");

        assertThat(user, not(isPresent()));
    }

    @Test
    public void findByUsername_existingUser() {
        // when
        User user = userRepository.findByUsername("Foo").get();

        // then
        validateUser(user, userId, unpersistedUser);
    }

    @Test
    public void findByEmail_unknownUser() {
        Optional<User> user = userRepository.findByEmail("unknown");

        assertThat(user, not(isPresent()));
    }

    @Test
    public void findByEmail_existingUser() {
        // when
        User user = userRepository.findByEmail("foo@bar.com").get();

        // then
        validateUser(user, userId, unpersistedUser);
    }

    @Test
    public void getUsers() {
        // when
        List<User> users = userRepository.getUsers();

        // then
        assertThat(users.size(), is(2));

        for (User user : users) {
            if (user.getId().equals(userId)) {
                validateUser(user, userId, this.unpersistedUser);
            } else {
                validateUser(user, adminId, this.unpersistedAdmin);
            }
        }
    }

    private void validateUser(User user, UserId expectedId, User expectedUser) {
        errorCollector.checkThat(user.getId(), is(expectedId));
        errorCollector.checkThat(user.getUsername(), is(expectedUser.getUsername()));
        errorCollector.checkThat(user.getEmail(), is(expectedUser.getEmail()));
        errorCollector.checkThat(user.isVerified(), is(expectedUser.isVerified()));
        errorCollector.checkThat(user.getRole(), is(expectedUser.getRole()));
        errorCollector.checkThat(user.getHashedPassword(), notNullValue());
        errorCollector.checkThat(user.getSalt(), notNullValue());
    }
}