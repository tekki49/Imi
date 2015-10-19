package com.imi.rest.core;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.model.Number;

public interface NumberSearch {

    List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
            throws ClientProtocolException, IOException;

    void setServiceType(Number number);

}
