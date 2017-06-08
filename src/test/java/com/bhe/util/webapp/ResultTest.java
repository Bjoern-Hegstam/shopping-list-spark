package com.bhe.util.webapp;

import org.junit.Test;

import java.util.HashMap;

import static com.bhe.util.webapp.ResultBuilder.result;
import static org.junit.Assert.assertEquals;

public class ResultTest {
    @Test
    public void redirect() {
        // given
        String redirectPath = "REDIRECT_PATH";

        // when
        Result result = result().redirectTo(redirectPath);

        // then
        assertIsValid(result);
        assertEquals(redirectPath, result.redirectPath);
    }

    @Test
    public void render() {
        // given
        String templatePath = "TEMPLATE_PATH";
        HashMap<String, Object> model = new HashMap<>();

        // when
        Result result = result().render(templatePath, model);

        // then
        assertIsValid(result);
        assertEquals(templatePath, result.renderTemplatePath);
        assertEquals(model, result.renderModel);
    }

    @Test
    public void payload() {
        // given
        Object payload = new Object();

        // when
        Result result = result().returnPayload(payload);

        // then
        assertIsValid(result);
        assertEquals(payload, result.responsePayload);
    }

    private static void assertIsValid(Result result) {
        int trueCount = (result.isRedirect() ? 1 : 0) +
                (result.isPayloadResponse() ? 1 : 0) +
                (result.isRender() ? 1 : 0);
        assertEquals(1, trueCount);
    }
}