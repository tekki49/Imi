package com.imi.rest.core;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.Number;

public interface NumberSearch {

    List<Number> searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern)
                    throws ClientProtocolException, IOException, ImiException;

    void setServiceType(Number number);

}
