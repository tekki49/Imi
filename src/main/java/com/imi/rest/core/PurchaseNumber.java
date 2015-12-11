package com.imi.rest.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.PurchaseRequest;
import com.imi.rest.model.PurchaseResponse;

public interface PurchaseNumber {

    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, Country country,
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, PurchaseRequest purchaseRequest, String teamuuid)
            throws ClientProtocolException, IOException;

}
