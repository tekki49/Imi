package com.imi.rest.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpUtil {

    public static String defaultHttpGetHandler(String url, String authHash)
            throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("Authorization", "Basic " + authHash);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String response = client.execute(get, handler);
        client.close();
        return response;
    }

    public static String defaultHttpGetHandler(String url)
            throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String response = client.execute(get, handler);
        client.close();
        return response;
    }

}
