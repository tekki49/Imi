package com.imi.rest.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.PurchaseResponse;

public interface PurchaseNumber {

    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, Country country,
            ServiceConstants serviceTypeEnum)
                    throws ClientProtocolException, IOException, ImiException;

}
