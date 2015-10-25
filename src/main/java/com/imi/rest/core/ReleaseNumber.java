package com.imi.rest.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface ReleaseNumber {

    void releaseNumber(String number, String provider);

    void releaseNumber(String number, String provider, String countryIsoCode)
            throws ClientProtocolException, IOException;
}
