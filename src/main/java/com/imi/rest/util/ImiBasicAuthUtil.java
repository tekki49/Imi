package com.imi.rest.util;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

import com.imi.rest.dao.model.Provider;

public class ImiBasicAuthUtil {

	public static String getBasicAuthHash(String authId, String authToken) {
		String unhashedString = authId + ":" + authToken;
		byte[] authBytes = unhashedString.getBytes(StandardCharsets.UTF_8);
		return DatatypeConverter.printBase64Binary(authBytes);
	}

	public static String getBasicAuthHash(Provider provider) {
		return getBasicAuthHash(provider.getAuthId(), provider.getApiKey());
	}
}
