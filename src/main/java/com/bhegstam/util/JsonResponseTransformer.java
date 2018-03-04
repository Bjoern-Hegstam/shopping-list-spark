package com.bhegstam.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JsonResponseTransformer implements ResponseTransformer {
    @Override
    public String render(Object o) throws Exception {
        return new ObjectMapper()
                .findAndRegisterModules()
                .writeValueAsString(o);
    }
}
