package com.imi.rest.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class HttpUtil {

    public static String defaultHttpGetHandler(String url, String authHash)
            throws ClientProtocolException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authHash);
        HttpEntity<String> entity = new HttpEntity<String>("parameters",
                headers);
        entity = restTemplate.exchange(url, HttpMethod.GET, entity,
                String.class);
        String responseBody = entity.getBody();
        return responseBody;
    }

    public static String defaultHttpGetHandler(String url)
            throws ClientProtocolException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters",
                headers);
        entity = restTemplate.exchange(url, HttpMethod.GET, entity,
                String.class);
        String responseBody = entity.getBody();
        return responseBody;
    }

}
