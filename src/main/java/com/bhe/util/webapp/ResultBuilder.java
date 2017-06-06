package com.bhe.util.webapp;

import java.util.HashMap;
import java.util.Map;

public class ResultBuilder {
    private String redirectPath;
    private Object responsePayload;
    private String renderTemplatePath;
    private Map<String, Object> renderModel;

    private ResultBuilder() {
    }

    public static ResultBuilder result() {
        return new ResultBuilder();
    }

    public Result redirectTo(String path) {
        redirectPath = path;
        return build();
    }

    public Result returnPayload(Object payload) {
        responsePayload = payload;
        return build();
    }

    public Result render(String templatePath) {
        return render(templatePath, new HashMap<>());
    }

    public Result render(String templatePath, Map<String, Object> model) {
        this.renderTemplatePath = templatePath;
        this.renderModel = model;
        return build();
    }

    private Result build() {
        return new Result(
                redirectPath,
                responsePayload,
                renderTemplatePath,
                renderModel
        );
    }
}
