package com.imi.rest.core.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.CountrySearch;
import com.imi.rest.core.NumberSearch;
import com.imi.rest.model.Country;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.util.HttpUtil;

@Component
public class NexmoSearchImpl implements NumberSearch, CountrySearch, UrlConstants,
        ProviderConstants {

    @Override
    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
            throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType,
                pattern, phoneSearchResult, Integer.MIN_VALUE, 1);
        return phoneSearchResult;
    }

    void searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            List<Number> phoneSearchResult, int count, int index)
            throws ClientProtocolException, IOException {
        String nexmoPhoneSearchUrl = NEXMO_PHONE_SEARCH_URL;
        if (Integer.MIN_VALUE == count) {
            index = 1;
        } else if (count - (index * 100) > 0) {
            index++;
        } else {
            index = -1;
            return;
        }
        nexmoPhoneSearchUrl = nexmoPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{api_key}", "a5eb8aa1")
                .replace("{api_secret}", "b457a519")
                .replace("{pattern}", pattern)
                .replace("{features}", serviceTypeEnum.toString().toUpperCase())
                .replace("{index}", "" + index);
        String response = HttpUtil.defaultHttpGetHandler(nexmoPhoneSearchUrl);
        ObjectMapper mapper = new ObjectMapper();
        NumberResponse numberResponse = mapper.readValue(response,
                NumberResponse.class);
        if (numberResponse == null)
            return;
        List<Number> nexmoNumberList = numberResponse == null ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        for (Number nexmoNumber : nexmoNumberList) {
            if (nexmoNumber != null) {
                setServiceType(nexmoNumber);
                nexmoNumber.setProvider(NEXMO);
                phoneSearchResult.add(nexmoNumber);
            }
        }
        count = numberResponse.getCount();
        searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType,
                pattern, phoneSearchResult, count, index);
    }

    @Override
    public void setServiceType(Number number) {
        List<String> features = number.getFeatures();
        if (features.contains(ServiceConstants.SMS.name())
                && features.contains(ServiceConstants.VOICE.name())) {
            number.setServiceType(ServiceConstants.BOTH.name());
            number.setSmsEnabled(true);
            number.setVoiceEnabled(true);
        } else if (features.contains(ServiceConstants.SMS.name())) {
            number.setServiceType(ServiceConstants.SMS.name());
            number.setSmsEnabled(true);
        } else {
            number.setServiceType(ServiceConstants.VOICE.name());
            number.setVoiceEnabled(true);
        }

    }
    @Override
    public Set<Country> importCountries()
            throws FileNotFoundException, JsonParseException, JsonMappingException, IOException {
        Set<Country> countriesSet = new HashSet<Country>();
        return countriesSet;
    }
}
