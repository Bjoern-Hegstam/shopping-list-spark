package com.bhe.util.webapp;

import java.util.HashMap;
import java.util.Map;

public class Result {
    public final String redirectPath;
    public final Object responsePayload;
    public final String renderTemplatePath;
    public final Map<String, Object> renderModel;

    public Result(String redirectPath, Object responsePayload, String renderTemplatePath, Map<String, Object> renderModel) {
        this.redirectPath = redirectPath;
        this.responsePayload = responsePayload;
        this.renderTemplatePath = renderTemplatePath;
        this.renderModel = renderModel != null ? renderModel : new HashMap<>();
    }
}
