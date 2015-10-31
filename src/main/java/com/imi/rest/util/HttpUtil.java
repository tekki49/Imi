package com.imi.rest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.imi.rest.exception.ImiException;

public class HttpUtil {

    public static String defaultHttpGetHandler(String url, String authHash)
            throws ClientProtocolException, IOException, ImiException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + authHash);
        HttpEntity<String> entity = new HttpEntity<String>("parameters",
                headers);
        try {
            entity = restTemplate.exchange(url, HttpMethod.GET, entity,
                    String.class);
        } catch (HttpClientErrorException e) {
            throw new ImiException("Rest Exception",
                    "Exception while getting response from url " + url
                            + " With status code " + e.getStatusCode()
                            + " and status text " + e.getStatusText());
        }
        String responseBody = entity.getBody();
        return responseBody;
    }

    public static String defaultHttpGetHandler(String url)
            throws ClientProtocolException, IOException, ImiException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>("parameters",
                headers);
        try {
            entity = restTemplate.exchange(url, HttpMethod.GET, entity,
                    String.class);
        } catch (HttpClientErrorException e) {
            throw new ImiException("Rest Exception",
                    "Exception while getting response from url " + url
                            + " With status code " + e.getStatusCode()
                            + " and status text " + e.getStatusText());
        }
        String responseBody = entity.getBody();
        return responseBody;
    }

    public static String defaultHttpPostHandler(String url,
            Map<String, String> requestBody, String authHash)
                    throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Authorization", "Basic " + authHash);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        for (String key : requestBody.keySet()) {
            nameValuePairs
                    .add(new BasicNameValuePair(key, requestBody.get(key)));
        }
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        return EntityUtils.toString(response.getEntity());
    }
}
