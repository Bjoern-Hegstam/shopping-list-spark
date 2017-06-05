package com.bhe.user;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {
    @Test
    public void testPasswordValidation() {
        // given
        User user = new User("foo", "bar");

        // then
        assertTrue(user.passwordIsValid("bar"));
        assertFalse(user.passwordIsValid("baz"));
    }
}