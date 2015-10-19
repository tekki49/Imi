package com.imi.rest.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class TwilioNumberSearchImpl implements NumberSearch,UrlConstants,ProviderConstants {

    @Override
    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
            throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        String twilioPhoneSearchUrl = TWILIO_PHONE_SEARCH_URL;
        String servicesString = generateTwilioCapabilities(serviceTypeEnum);
        twilioPhoneSearchUrl = twilioPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{services}", servicesString)
                .replace("{pattern}", pattern);
        String response = HttpUtil.defaultHttpGetHandler(twilioPhoneSearchUrl,
                BasicAuthUtil.getBasicAuthHash(TWILIO));
        ObjectMapper mapper = new ObjectMapper();
        NumberResponse numberResponse = mapper.readValue(response,
                NumberResponse.class);
        List<Number> twilioNumberList = numberResponse == null ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        for (Number twilioNumber : twilioNumberList) {
            if (twilioNumber != null) {
                setServiceType(twilioNumber);
                twilioNumber.setProvider(TWILIO);
                phoneSearchResult.add(twilioNumber);
            }
        }
        return phoneSearchResult;
    }

    @Override
    public void setServiceType(Number number) {
        Map<String, Boolean> capabilties = number.getCapabilities();
        if (capabilties.get("voice") && capabilties.get("SMS")) {
            number.setServiceType(ServiceConstants.BOTH.name());
            number.setSmsEnabled(true);
            number.setVoiceEnabled(true);
        } else if (capabilties.get("SMS")) {
            number.setServiceType(ServiceConstants.SMS.name());
            number.setSmsEnabled(true);
        } else {
            number.setServiceType(ServiceConstants.VOICE.name());
            number.setVoiceEnabled(true);
        }
    }

    private String generateTwilioCapabilities(ServiceConstants serviceTypeEnum) {
        String servicesString = null;
        switch (serviceTypeEnum) {
        case SMS:
            servicesString = "SmsEnabled=true";
            break;
        case VOICE:
            servicesString = "VoiceEnabled=true";
            break;
        case BOTH:
            servicesString = "SmsEnabled=true&VoiceEnabled=true";
            break;
        default:
            break;
        }
        return servicesString;
    }

}
