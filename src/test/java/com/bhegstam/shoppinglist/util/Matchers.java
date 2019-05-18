package com.bhegstam.shoppinglist.util;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.Instant;
import java.util.Collection;
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

    public static <T> Matcher<Collection<T>> isEmpty() {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("is empty");
            }

            @Override
            protected boolean matchesSafely(Collection<T> collection) {
                return collection.isEmpty();
            }
        };
    }

    public static Matcher<Instant> isAfter(Instant value) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Instant instant) {
                return instant.isAfter(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is after " + value);
            }
        };
    }

    public static Matcher<Instant> isAtOrAfter(Instant value) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Instant instant) {
                return instant.isAfter(value) || instant.equals(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is at or after " + value);
            }
        };
    }
}
