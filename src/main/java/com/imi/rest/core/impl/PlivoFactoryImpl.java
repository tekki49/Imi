package com.imi.rest.core.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.constants.ForexConstants;
import com.imi.rest.constants.NumberTypeConstants;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.CountrySearch;
import com.imi.rest.core.NumberSearch;
import com.imi.rest.core.PurchaseNumber;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.BalanceResponse;
import com.imi.rest.model.Country;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PlivioPurchaseResponse;
import com.imi.rest.model.PlivoAccountResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.CountrySearchService;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.DataFormatUtils;
import com.imi.rest.util.HttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class PlivoFactoryImpl
        implements NumberSearch, CountrySearch, PurchaseNumber, UrlConstants,
        ProviderConstants, NumberTypeConstants, ForexConstants {

    private static final String PLIVO_CSV_FILE_PATH = "/plivo_inbound_rates.csv";
    // max no of numbers to be obtained
    private static final int THRESHHOLD = 100;
    private static final Logger LOG = Logger
            .getLogger(CountrySearchService.class);

    @Override
    public List<Number> searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern)
                    throws ClientProtocolException, IOException, ImiException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        /*
         * String plivioPhoneSearchUrl = PLIVO_PHONE_SEARCH_URL;
         * plivioPhoneSearchUrl = plivioPhoneSearchUrl .replace("{country_iso}",
         * countryIsoCode) .replace("{type}", numberType.toLowerCase())
         * .replace("{services}", serviceTypeEnum.toString())
         * .replace("{pattern}", "*"+pattern+"*"); String response; try {
         * response = HttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
         * BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
         * provider.getApiKey())); } catch (ImiException e) { return
         * phoneSearchResult; } NumberResponse numberResponse =
         * ImiJsonUtil.deserialize(response, NumberResponse.class); List<Number>
         * plivioNumberList = numberResponse == null ? new ArrayList<Number>() :
         * numberResponse.getObjects() == null ? new ArrayList<Number>() :
         * numberResponse.getObjects(); for (Number plivioNumber :
         * plivioNumberList) { if (plivioNumber != null) {
         * plivioNumber.setProvider(PLIVO); setServiceType(plivioNumber);
         * phoneSearchResult.add(plivioNumber); } }
         */
        String type = "any";
        if (numberType.equalsIgnoreCase(LANDLINE)) {
            type = "fixed";
        } else if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "mobile ";
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "tollfree";
        } else if (numberType.equalsIgnoreCase(LOCAL)) {
            type = "fixed";
        }
        searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, type,
                pattern, phoneSearchResult, 0);
        return phoneSearchResult;
    }

    void searchPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            List<Number> phoneSearchResult, int offset)
                    throws ClientProtocolException, IOException, ImiException {
        if (offset >= THRESHHOLD)
            return;
        String plivioPhoneSearchUrl = PLIVO_PHONE_SEARCH_URL;
        plivioPhoneSearchUrl = plivioPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{type}", numberType)
                .replace("{services}", serviceTypeEnum.toString())
                .replace("{pattern}", "*" + pattern + "*");
        if (offset > 0) {
            plivioPhoneSearchUrl = plivioPhoneSearchUrl + "&offset=" + offset;
        }
        String response = "";
        try {
            response = HttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
                    BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                            provider.getApiKey()));
        } catch (ImiException e) {
            return;
        }
        NumberResponse numberResponse = ImiJsonUtil.deserialize(response,
                NumberResponse.class);
        List<Number> plivioNumberList = numberResponse == null
                ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        for (Number plivioNumber : plivioNumberList) {
            if (plivioNumber != null) {
                plivioNumber.setProvider(PLIVO);
                setServiceType(plivioNumber);
                plivioNumber.setPriceUnit("USD");
                String monthlyRentalRateInGBP = DataFormatUtils.forexConvert(
                        USD_GBP, plivioNumber.getMonthlyRentalRate());
                plivioNumber.setMonthlyRentalRate(monthlyRentalRateInGBP);
                String voiceRateInGBP = DataFormatUtils.forexConvert(USD_GBP,
                        plivioNumber.getVoiceRate());
                plivioNumber.setVoiceRate(voiceRateInGBP);
                if (numberType.equalsIgnoreCase("fixed")
                        || numberType.equalsIgnoreCase("local")) {
                    numberType = "Landline";
                }
                plivioNumber.setType(numberType.toLowerCase());
                phoneSearchResult.add(plivioNumber);
            }
        }
        if (numberResponse.getMeta() != null
                && numberResponse.getMeta().getNext() != null
                && !numberResponse.getMeta().getNext().equals("")) {
            offset = numberResponse.getMeta().getOffset()
                    + numberResponse.getMeta().getLimit();
            searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode,
                    numberType, pattern, phoneSearchResult, offset);
        }
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
    public Set<Country> importCountries() throws FileNotFoundException,
            JsonParseException, JsonMappingException, IOException {
        Set<Country> countrySet = new TreeSet<Country>();
        String line = "";
        String splitBy = ",";
        BufferedReader reader = null;
        try {
            InputStream in = getClass()
                    .getResourceAsStream(PLIVO_CSV_FILE_PATH);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(splitBy);
                if (counter != 0 && row.length > 1) {
                    Country country = new Country();
                    country.setCountry(row[0]);
                    country.setIsoCountry(row[2]);
                    country.setCountryCode(row[1]);
                    countrySet.add(country);
                }
                counter++;
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
        return countrySet;
    }

    public PurchaseResponse purchaseNumber(String number, Provider provider,
            com.imi.rest.dao.model.Country country)
                    throws ClientProtocolException, IOException, ImiException {
        String plivioPurchaseUrl = PLIVO_PURCHASE_URL;
        String plivioNumber = number.trim() + country.getCountryCode().trim();
        plivioPurchaseUrl = plivioPurchaseUrl.replace("{number}", plivioNumber);
        String response;
        response = HttpUtil.defaultHttpPostHandler(plivioPurchaseUrl,
                new HashMap<String, String>(), BasicAuthUtil.getBasicAuthHash(
                        provider.getAuthId(), provider.getApiKey()));
        PlivioPurchaseResponse plivioPurchaseResponse = ImiJsonUtil
                .deserialize(response, PlivioPurchaseResponse.class);
        return null;
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        String plivioReleaseurl = PLIVO_RELEASE_URL;
        String plivioNumber = number.trim() + countryIsoCode.trim();
        plivioReleaseurl = plivioReleaseurl.replace("{number}", plivioNumber);
        try {
            String response = HttpUtil.defaultHttpGetHandler(plivioReleaseurl,
                    BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                            provider.getApiKey()));
        } catch (ImiException e) {
            // TODO need to validate the response
        }
    }
    
    public BalanceResponse checkBalance(Provider provider) throws ClientProtocolException, IOException {
        String plivoAccountBalanceurl = PLIVO_ACCOUNT_BALANCE_URL;  
        BalanceResponse balanceResponse = new BalanceResponse();
        try {
        	 String response = HttpUtil.defaultHttpGetHandler(plivoAccountBalanceurl,
                    BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                            provider.getApiKey()));
            PlivoAccountResponse plivoAccountResponse = ImiJsonUtil
                    .deserialize(response, PlivoAccountResponse.class);
            balanceResponse.setValue(plivoAccountResponse.getCash_credits());
        } catch (ImiException e) {
            // TODO need to validate the response
        }
        return balanceResponse;
    }

}
