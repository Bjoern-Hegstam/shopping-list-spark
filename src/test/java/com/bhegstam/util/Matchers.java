package com.bhegstam.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Optional;

public class Matchers {
    public static <T> Matcher<Optional<T>> isPresentAnd(Matcher<T> matcher) {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is present and ");
                matcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(Optional<T> item) {
                return item.map(matcher::matches).orElse(false);
            }
        };
    }

    public static <T> Matcher<Optional<T>> isPresent() {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is present");
            }

            @Override
            protected boolean matchesSafely(Optional<T> item) {
                return item.isPresent();
            }
        };
    }
}
