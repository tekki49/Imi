package com.imi.rest.core.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.HttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class NexmoFactoryImpl
        implements NumberSearch, CountrySearch, PurchaseNumber, UrlConstants,
        ProviderConstants, ForexConstants, NumberTypeConstants {

    private String nexmoPricingResponse;
    private static final String NEXMO_PRICING_FILE_PATH = "/nexmo_pricing.xls";
    private static final int THRESHHOLD = 100;

    @Override
    public List<Number> searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern)
                    throws ClientProtocolException, IOException, ImiException {
        nexmoPricingResponse = null;
        List<Number> phoneSearchResult = new ArrayList<Number>();
        String type = "landline";
        if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "mobile";
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "tollfree";
        }
        searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, type,
                pattern, phoneSearchResult, Integer.MIN_VALUE, 0);
        return phoneSearchResult;
    }

    void searchPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            List<Number> phoneSearchResult, int count, int index)
                    throws ClientProtocolException, IOException, ImiException {
        if (index * 100 >= THRESHHOLD)
            return;
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
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey())
                .replace("{pattern}", pattern)
                .replace("{features}", serviceTypeEnum.toString().toUpperCase())
                .replace("{index}", "" + index);
        String response = "";
        try {
            response = HttpUtil.defaultHttpGetHandler(nexmoPhoneSearchUrl);
        } catch (ImiException e) {
        }
        NumberResponse numberResponse = ImiJsonUtil.deserialize(response,
                NumberResponse.class);
        if (numberResponse == null)
            return;
        List<Number> nexmoNumberList = numberResponse == null
                ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        if (getNexmoPricingResponse() == null) {
            setNexmoPricingResponse(getNexmoPricing(NEXMO_PRICING_FILE_PATH));
        }
        List<CountryPricing> countryPricingList = ImiJsonUtil
                .deserializeList(nexmoPricingResponse, CountryPricing.class);
        Map<String, CountryPricing> countryPricingMap = new HashMap<String, CountryPricing>();
        for (CountryPricing countryPricing : countryPricingList) {
            countryPricingMap.put(countryPricing.getCountryIsoCode(),
                    countryPricing);
        }
        for (Number nexmoNumber : nexmoNumberList) {
            if (nexmoNumber != null) {
                setServiceType(nexmoNumber);
                CountryPricing countryPricing = countryPricingMap
                        .get(nexmoNumber.getCountry().toUpperCase());
                String type = "normal";
                if (nexmoNumber.getNumberType()
                        .equalsIgnoreCase("mobile-lvn")) {
                    nexmoNumber.setType("mobile");
                } else if (nexmoNumber.getNumberType()
                        .equalsIgnoreCase("local")) {
                    nexmoNumber.setType("landline");
                }
                nexmoNumber.setPriceUnit("EUR");
                nexmoNumber
                        .setMonthlyRentalRate(String.valueOf(Float
                                .parseFloat(countryPricing.getPricing()
                                        .get(type).get("monthlyRateInEuros"))
                        * EUR_GBP));
                nexmoNumber
                        .setVoiceRate(String.valueOf(Float
                                .parseFloat(countryPricing.getPricing()
                                        .get(type).get("voiceRateInEuros"))
                        * EUR_GBP));
                nexmoNumber.setCountry(countryPricing.getCountry());
                nexmoNumber.setProvider(NEXMO);
                phoneSearchResult.add(nexmoNumber);
            }
        }
        count = numberResponse.getCount();
        searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode,
                numberType, pattern, phoneSearchResult, count, index);
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
    public Set<Country> importCountries() throws FileNotFoundException,
            JsonParseException, JsonMappingException, IOException {
        Set<Country> countrySet = new HashSet<Country>();
        InputStream in = getClass()
                .getResourceAsStream(NEXMO_PRICING_FILE_PATH);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Country country = new Country();
            Row row = sheet.getRow(i);
            country.setIsoCountry(row.getCell(0).toString());
            country.setCountryCode(row.getCell(5).toString());
            country.setCountry(row.getCell(1).toString());
            countrySet.add(country);
        }
        in.close();
        workbook.close();
        return countrySet;
    }

    public PurchaseResponse purchaseNumber(String number, Provider provider,
            com.imi.rest.dao.model.Country country)
                    throws ClientProtocolException, IOException, ImiException {
        String nexmoPurchaseUrl = NEXMO_PURCHASE_URL;
        String nexmoNumber = number.trim() + country.getCountryCode().trim();
        nexmoPurchaseUrl = nexmoPurchaseUrl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey())
                .replace("{country}", "").replace("{msisdn}", nexmoNumber);
        String response = HttpUtil.defaultHttpGetHandler(nexmoPurchaseUrl);
        return null;
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        String nexmoReleaseUrl = NEXMO_RELEASE_URL;
        String nexmoNumber = number.trim() + countryIsoCode.trim();
        nexmoReleaseUrl = nexmoReleaseUrl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}",
                        provider.getApiKey().replace("{country}", "")
                                .replace("{msisdn}", nexmoNumber));
        try {
            String response = HttpUtil.defaultHttpGetHandler(nexmoReleaseUrl,
                    BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                            provider.getApiKey()));
        } catch (ImiException e) {
        }
    }

    public String getNexmoPricingResponse() {
        return nexmoPricingResponse;
    }

    public void setNexmoPricingResponse(String nexmoPricingResponse) {
        this.nexmoPricingResponse = nexmoPricingResponse;
    }

    public String getNexmoPricing(String fileName) throws IOException {
        InputStream in = getClass().getResourceAsStream(fileName);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet sheet = workbook.getSheetAt(3);
        String response = "";
        List<CountryPricing> nexmoPricingList = new ArrayList<CountryPricing>();
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            CountryPricing countryPricing = new CountryPricing();
            Row row = sheet.getRow(i);
            countryPricing
                    .setCountry(row.getCell(0).toString().split(" - ")[1]);
            countryPricing.setCountryIsoCode(
                    row.getCell(0).toString().split(" - ")[0]);
            Map<String, String> priceMap = new HashMap<String, String>();
            Map<String, Map<String, String>> pricing = new HashMap<String, Map<String, String>>();
            priceMap.put("monthlyRateInEuros", row.getCell(1).toString());
            priceMap.put("voiceRateInEuros", row.getCell(2).toString());
            pricing.put("normal", priceMap);
            priceMap = new HashMap<String, String>();
            priceMap.put("monthlyRateInEuros", row.getCell(3).toString());
            priceMap.put("voiceRateInEuros", row.getCell(4).toString());
            pricing.put("tollfree", priceMap);
            countryPricing.setPricing(pricing);
            nexmoPricingList.add(countryPricing);
        }
        response += ImiJsonUtil.serialize(nexmoPricingList);
        in.close();
        workbook.close();
        return response;
    }
    
    public BalanceResponse checkBalance(Provider provider) throws ClientProtocolException, IOException {
        String nexoAccountBalanceurl = NEXMO_ACCOUNT_BALANCE_URL;
        nexoAccountBalanceurl = nexoAccountBalanceurl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}",
                        provider.getApiKey());
        BalanceResponse balanceResponse = new BalanceResponse();
        try {
            String response = HttpUtil.defaultHttpGetHandler(nexoAccountBalanceurl,
                    BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()));
            balanceResponse = ImiJsonUtil.deserialize(response, BalanceResponse.class);
        } catch (ImiException e) {
        }
        return balanceResponse;
    }

}
