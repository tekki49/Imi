package com.imi.rest.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImiJsonUtil {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static String getJSONString(String key, String value) throws JsonProcessingException {
		Map<String, String> myMap = new HashMap<String, String>();
		myMap.put(key, value);
		return OBJECT_MAPPER.writeValueAsString(myMap);
	}

	public static String getJSONString(String key, Object value) throws JsonProcessingException {
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put(key, value);
		return OBJECT_MAPPER.writeValueAsString(myMap);
	}

	public static <T> T deserialize(String json, Class<T> classType)
			throws JsonParseException, JsonMappingException, IOException {
		return OBJECT_MAPPER.readValue(json, classType);
	}

	public static <T> List<T> deserializeList(String json, Class<T> classType)
			throws JsonParseException, JsonMappingException, IOException {
		return OBJECT_MAPPER.readValue(json,
				OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, classType));
	}

	public static String serialize(Object obj) throws JsonProcessingException {
		return OBJECT_MAPPER.writeValueAsString(obj);
	}
}
