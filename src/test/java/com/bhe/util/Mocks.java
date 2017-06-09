package com.bhe.util;

import com.bhe.util.webapp.Request;
import com.bhe.util.webapp.Session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Mocks {
    public static Request mockRequest() {
        Request request = mock(Request.class);
        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        return request;
    }
}
