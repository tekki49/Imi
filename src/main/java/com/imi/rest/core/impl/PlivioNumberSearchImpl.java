package com.imi.rest.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.NumberSearch;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.HttpUtil;

@Component
public class PlivioNumberSearchImpl implements NumberSearch, UrlConstants,
        ProviderConstants {

    @Override
    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
            throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        String plivioPhoneSearchUrl = PLIVIO_PHONE_SEARCH_URL;
        plivioPhoneSearchUrl = plivioPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{type}", numberType.toLowerCase())
                .replace("{services}", serviceTypeEnum.toString())
                .replace("{pattern}", pattern);
        if (pattern.equals("")) {
            plivioPhoneSearchUrl = plivioPhoneSearchUrl
                    .replace("&pattern=", "");
        }
        ObjectMapper mapper = new ObjectMapper();
        String response = HttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
                BasicAuthUtil.getBasicAuthHash(PLIVIO));
        NumberResponse numberResponse = mapper.readValue(response,
                NumberResponse.class);
        List<Number> plivioNumberList = numberResponse == null ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        for (Number plivioNumber : plivioNumberList) {
            if (plivioNumber != null) {
                plivioNumber.setProvider(PLIVIO);
                setServiceType(plivioNumber);
                phoneSearchResult.add(plivioNumber);
            }
        }
        return phoneSearchResult;
    }

    @Override
    public void setServiceType(Number number) {
        if (number.isSmsEnabled() && number.isVoiceEnabled()) {
            number.setServiceType(ServiceConstants.BOTH.name());
        } else if (number.isSmsEnabled()) {
            number.setServiceType(ServiceConstants.SMS.name());
        } else {
            number.setServiceType(ServiceConstants.VOICE.name());
        }
    }

}
