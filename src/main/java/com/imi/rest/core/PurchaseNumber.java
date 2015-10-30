package com.imi.rest.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.PurchaseResponse;

public interface PurchaseNumber {

    public PurchaseResponse purchaseNumber(String number,Provider provider, String countryIsoCode)
            throws ClientProtocolException, IOException;

}
