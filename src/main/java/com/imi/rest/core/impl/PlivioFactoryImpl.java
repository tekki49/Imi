package com.imi.rest.core.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.CountrySearch;
import com.imi.rest.core.NumberSearch;
import com.imi.rest.core.PurchaseNumber;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.Country;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PlivioPurchaseResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.CountrySearchService;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.HttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class PlivioFactoryImpl implements NumberSearch, CountrySearch,PurchaseNumber,
        UrlConstants, ProviderConstants {

    private static final String PLIVIO_CSV_FILE_PATH = "/home/hemanth/Desktop/PLIVIO_COUNTRIES_WITH_ISO.csv";
    private static final Logger LOG = Logger
            .getLogger(CountrySearchService.class);

    @Override
    public List<Number> searchPhoneNumbers(Provider provider,ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
                    throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        String plivioPhoneSearchUrl = PLIVIO_PHONE_SEARCH_URL;
        plivioPhoneSearchUrl = plivioPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{type}", numberType.toLowerCase())
                .replace("{services}", serviceTypeEnum.toString())
                .replace("{pattern}", pattern);
        String response = HttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
                BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),provider.getApiKey()));
        NumberResponse numberResponse = ImiJsonUtil.deserialize(response,
                NumberResponse.class);
        List<Number> plivioNumberList = numberResponse == null
                ? new ArrayList<Number>()
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

    @Override
    public Set<Country> importCountries(Provider provider) throws FileNotFoundException,
            JsonParseException, JsonMappingException, IOException {
        Set<Country> countriesSet = new TreeSet<Country>();
        String line = "";
        String splitBy = ",";
        BufferedReader reader = null;
        try {
            FileReader fileReader = new FileReader(PLIVIO_CSV_FILE_PATH);
            reader = new BufferedReader(fileReader);
            while ((line = reader.readLine()) != null) {
                Country country = new Country();
                country.setCountry(line.split(splitBy)[0]);
                country.setIsoCountry(line.split(splitBy)[1]);
                countriesSet.add(country);
            }
        } catch (FileNotFoundException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                    LOG.error(e2);
                }
            }
        }
        return countriesSet;
    }

    public PurchaseResponse purchaseNumber(String number,Provider provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        String plivioPurchaseUrl = PLIVIO_PURCHASE_URL;
        String plivioNumber = number.trim() + countryIsoCode.trim();
        plivioPurchaseUrl = plivioPurchaseUrl.replace("{number}", plivioNumber);
        String response = HttpUtil.defaultHttpGetHandler(plivioPurchaseUrl,
                BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),provider.getApiKey()));
        PlivioPurchaseResponse plivioPurchaseResponse = ImiJsonUtil.deserialize(response, PlivioPurchaseResponse.class);
        return null;
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        String plivioReleaseurl = PLIVIO_RELEASE_URL;
        String plivioNumber = number.trim() + countryIsoCode.trim();
        plivioReleaseurl = plivioReleaseurl.replace("{number}", plivioNumber);
        String response = HttpUtil.defaultHttpGetHandler(plivioReleaseurl,
                BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),provider.getApiKey()));
    }

}
