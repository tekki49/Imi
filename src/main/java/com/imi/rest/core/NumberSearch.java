package com.imi.rest.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.NumberResponse;

public interface NumberSearch {

    void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String index,
            NumberResponse numberResponse, String markup)
            throws ClientProtocolException, IOException;

}
