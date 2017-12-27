package com.bhegstam.user.domain;

import com.bhegstam.user.UserRepositoryInMem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserRegistrationTest {

    private static final String UNUSED_USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String UNUSED_EMAIL = "email@domain.com";

    private static final String INVALID_USERNANE = "";
    private static final String INVALID_EMAIL = "";

    private UserRepositoryInMem userRepository;
    private UserRegistration userRegistration;
    private User existingUser;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepositoryInMem();
        userRegistration = new UserRegistration(userRepository);

        existingUser = new User(
                "John",
                "jp93",
                "john.hammond@domain.com"
        );
    }

    @Test
    public void register_success() {
        // given
        User user = new User(
                UNUSED_USERNAME,
                PASSWORD,
                UNUSED_EMAIL
        );

        // when
        boolean result = userRegistration.register(user);

        // then
        assertTrue(result);
    }

    @Test
    public void register_whenUsernameIsInvalid() {
        // given
        User user = new User(
                INVALID_USERNANE,
                PASSWORD,
                UNUSED_EMAIL
        );

        // when
        boolean result = userRegistration.register(user);

        // then
        assertFalse(result);
    }

    @Test
    public void register_whenUsernameAlreadyTaken() {
        // given
        userRepository.create(existingUser);
        User user = new User(
                existingUser.getUsername(),
                PASSWORD,
                UNUSED_EMAIL
        );

        // when
        boolean result = userRegistration.register(user);

        // then
        assertFalse(result);
    }

    @Test
    public void register_whenEmailIsInvalid() {
        // given
        User user = new User(
                UNUSED_USERNAME,
                PASSWORD,
                INVALID_EMAIL
        );

        // when
        boolean result = userRegistration.register(user);

        // then
        assertFalse(result);
    }

    @Test
    public void register_whenEmailAlreadyInUse() {
        // given
        userRepository.create(existingUser);
        User user = new User(
                UNUSED_USERNAME,
                PASSWORD,
                existingUser.getEmail()
        );

        // when
        boolean result = userRegistration.register(user);

        // then
        assertFalse(result);
    }
}