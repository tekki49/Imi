package com.imi.rest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.imi.rest.model.GenericRestResponse;

public class ImiHttpUtil {

	public static GenericRestResponse defaultHttpGetHandler(String url, String authHash)
			throws ClientProtocolException, IOException {
		GenericRestResponse restResponse = new GenericRestResponse();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.setHeader("Authorization", "Basic " + authHash);
		HttpResponse httpResponse = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String responsebody = "";
		String line = "";
		while ((line = rd.readLine()) != null) {
			responsebody += line;
		}
		restResponse.setResponseBody(responsebody);
		restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
		return restResponse;
	}

	public static GenericRestResponse defaultHttpGetHandler(String url) throws ClientProtocolException, IOException {
		GenericRestResponse restResponse = new GenericRestResponse();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse httpResponse = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String responsebody = "";
		String line = "";
		while ((line = rd.readLine()) != null) {
			responsebody += line;
		}
		restResponse.setResponseBody(responsebody);
		restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
		return restResponse;
	}

	public static GenericRestResponse defaultHttpPostHandler(String url, Map<String, String> requestBody,
			String authHash, String contentType) throws ClientProtocolException, IOException {
		GenericRestResponse restResponse = new GenericRestResponse();
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Authorization", "Basic " + authHash);
		if (contentType != null) {
			httppost.setHeader("Content-Type", contentType);
		}
		JSONObject json = new JSONObject();
		for (String key : requestBody.keySet()) {
			json.put(key, requestBody.get(key));
		}
		StringEntity params = new StringEntity(json.toString());
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : requestBody.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, requestBody.get(key)));
		}
		if (!ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(contentType)) {
			params = new UrlEncodedFormEntity(nameValuePairs);
		}
		httppost.setEntity(params);
		HttpResponse httpResponse = httpclient.execute(httppost);
		restResponse.setResponseBody(EntityUtils.toString(httpResponse.getEntity()));
		restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
		return restResponse;
	}

	public static GenericRestResponse defaultHttpPostHandler(String url, String contentType)
			throws ClientProtocolException, IOException {
		GenericRestResponse restResponse = new GenericRestResponse();
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		if (contentType != null) {
			httppost.setHeader("Content-Type", contentType);
		}
		HttpResponse httpResponse = httpclient.execute(httppost);
		restResponse.setResponseBody(EntityUtils.toString(httpResponse.getEntity()));
		restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
		return restResponse;
	}

	public static GenericRestResponse defaultHttpDeleteHandler(String url, Map<String, String> requestBody,
			String authHash, String contentType) throws IOException {
		GenericRestResponse restResponse = new GenericRestResponse();
		HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
		httpDelete.setHeader("Authorization", "Basic " + authHash);
		HttpClient httpclient = HttpClientBuilder.create().build();
		JSONObject json = new JSONObject();
		for (String key : requestBody.keySet()) {
			json.put(key, requestBody.get(key));
		}
		StringEntity params = new StringEntity(json.toString());
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : requestBody.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, requestBody.get(key)));
		}
		if (!ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(contentType)) {
			params = new UrlEncodedFormEntity(nameValuePairs);
		}
		httpDelete.setEntity(params);
		HttpResponse httpResponse = httpclient.execute(httpDelete);
		if (httpResponse.getEntity() != null) {
			restResponse.setResponseBody(EntityUtils.toString(httpResponse.getEntity()));
		}
		restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
		return restResponse;
	}

	public static GenericRestResponse defaultHttpPostHandler(String url, Map<String, String> requestBody,
			String contentType) throws ParseException, IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);
		if (contentType != null) {
			httppost.setHeader("Content-Type", contentType);
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : requestBody.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, requestBody.get(key)));
		}
		StringEntity params = new UrlEncodedFormEntity(nameValuePairs);
		httppost.setEntity(params);
		HttpResponse httpResponse = httpclient.execute(httppost);
		GenericRestResponse restResponse = new GenericRestResponse();
		restResponse.setResponseBody(EntityUtils.toString(httpResponse.getEntity()));
		restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
		return restResponse;
	}
}

@NotThreadSafe
class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
	public static final String METHOD_NAME = "DELETE";

	public String getMethod() {
		return METHOD_NAME;
	}

	public HttpDeleteWithBody(final String uri) {
		super();
		setURI(URI.create(uri));
	}

	public HttpDeleteWithBody(final URI uri) {
		super();
		setURI(uri);
	}

	public HttpDeleteWithBody() {
		super();
	}
}