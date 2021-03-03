package com.bhegstam.shoppinglist.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void checkAgainstUserPassword() {
        // given
        User user = new User("foo", "bar", "email@domain.com");

        // then
        assertTrue(user.hasPassword("bar"));
        assertFalse(user.hasPassword("baz"));
    }

    @Test
    public void validateEmail() {
        assertTrue(userWithEmail("first@domain.com").hasValidEmail());
        assertTrue(userWithEmail("first.last@domain.com").hasValidEmail());

        assertFalse(userWithEmail("").hasValidEmail());
    }

    private User userWithEmail(String email) {
        return new User(
                "username",
                "password",
                email
        );
    }
}
