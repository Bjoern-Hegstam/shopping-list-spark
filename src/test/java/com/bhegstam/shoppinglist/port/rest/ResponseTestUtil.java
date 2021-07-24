package com.bhegstam.shoppinglist.port.rest;

import org.hamcrest.Matchers;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;

public class ResponseTestUtil {
    public static final int UNPROCESSABLE_ENTITY = 422;

    public static void assertResponseStatus(Response response, Response.Status status) {
        assertThat(response.getStatus(), Matchers.is(status.getStatusCode()));
    }

    public static void assertResponseStatus(Response response, int statusCode) {
        assertThat(response.getStatus(), Matchers.is(statusCode));
    }
}
