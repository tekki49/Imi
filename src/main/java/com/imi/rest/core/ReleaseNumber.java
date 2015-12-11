package com.imi.rest.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.imi.rest.dao.model.Provider;

public interface ReleaseNumber {

	void releaseNumber(String number, Provider provider);

	void releaseNumber(String number, Provider provider, String countryIsoCode)
			throws ClientProtocolException, IOException;
}
