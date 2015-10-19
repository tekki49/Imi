package com.imi.rest.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImiJsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objMapper) {
        objectMapper = objMapper;
    }

    public static String getJSONString(String key, String value)
            throws JsonProcessingException {
        Map<String, String> myMap = new HashMap<String, String>();
        myMap.put(key, value);
        return objectMapper.writeValueAsString(myMap);
    }

    public static String getJSONString(String key, Object value)
            throws JsonProcessingException {
        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put(key, value);
        return objectMapper.writeValueAsString(myMap);
    }

}
