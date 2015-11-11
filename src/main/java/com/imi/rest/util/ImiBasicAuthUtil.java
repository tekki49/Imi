package com.imi.rest.util;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;

public class ImiBasicAuthUtil {

    public static String getBasicAuthHash(String authId, String authToken) {
        String unhashedString = authId + ":" + authToken;
        byte[] authBytes = unhashedString.getBytes(StandardCharsets.UTF_8);
        return DatatypeConverter.printBase64Binary(authBytes);
    }

}
