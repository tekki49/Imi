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
import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONObject;
import org.json.XML;
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
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.BalanceResponse;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.DataFormatUtils;
import com.imi.rest.util.HttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class NexmoFactoryImpl
        implements NumberSearch, CountrySearch, PurchaseNumber, UrlConstants,
        ProviderConstants, ForexConstants, NumberTypeConstants {

    private String nexmoPricingResponse;
    private static final String NEXMO_PRICING_FILE_PATH = "/nexmo_pricing.xls";

    @Override
    public void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String nexmoIndex,
            NumberResponse numberResponse)
                    throws ClientProtocolException, IOException, ImiException {
        nexmoPricingResponse = null;
        List<Number> numberSearchList = numberResponse.getObjects() == null
                ? new ArrayList<Number>() : numberResponse.getObjects();
        String type = "landline";
        if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "mobile";
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "tollfree";
        }
        int index = 1;
        if (!"FIRST".equalsIgnoreCase(nexmoIndex)) {
            index = Integer.parseInt(nexmoIndex);
        } else {
            Meta meta = numberResponse.getMeta() == null ? new Meta()
                    : numberResponse.getMeta();
            meta.setPreviousNexmoIndex("FIRST");
            numberResponse.setMeta(meta);
        }
        searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, type,
                pattern, numberSearchList, index, numberResponse);
    }

    void searchPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            List<Number> numberSearchList, int index,
            NumberResponse numberResponse)
                    throws ClientProtocolException, IOException, ImiException {
        String nexmoPhoneSearchUrl = NEXMO_PHONE_SEARCH_URL;
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
        NumberResponse nexmoNumberResponse = ImiJsonUtil.deserialize(response,
                NumberResponse.class);
        if (nexmoNumberResponse == null)
            return;
        List<Number> nexmoNumberList = nexmoNumberResponse == null
                ? new ArrayList<Number>()
                : nexmoNumberResponse.getObjects() == null
                        ? new ArrayList<Number>()
                        : nexmoNumberResponse.getObjects();
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
                nexmoNumber.setMonthlyRentalRate(DataFormatUtils
                        .forexConvert(EUR_GBP, countryPricing.getPricing()
                                .get(type).get("monthlyRateInEuros")));
                nexmoNumber.setVoiceRate(DataFormatUtils.forexConvert(EUR_GBP,
                        countryPricing.getPricing().get(type)
                                .get("voiceRateInEuros")));
                nexmoNumber.setCountry(countryPricing.getCountry());
                nexmoNumber.setProvider(NEXMO);
                numberSearchList.add(nexmoNumber);
            }
        }
        if (nexmoNumberResponse.getCount() - index * 100 > 0) {
            Meta meta = numberResponse.getMeta() == null ? new Meta()
                    : numberResponse.getMeta();
            String nextNexmoIndex = null;
            String previousNexmoIndex = meta.getNextPlivoIndex();
            previousNexmoIndex = "" + index;
            nextNexmoIndex = "" + (index + 1);
            meta.setPreviousNexmoIndex(previousNexmoIndex);
            meta.setNextNexmoIndex(nextNexmoIndex);
            numberResponse.setMeta(meta);
        }
        numberResponse.setObjects(numberSearchList);
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

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        String nexmoReleaseUrl = NEXMO_RELEASE_URL;
        String nexmoNumber = number.trim() + countryIsoCode.trim();
        nexmoReleaseUrl = nexmoReleaseUrl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}",
                        provider.getApiKey().replace("{country}", "")
                                .replace("{msisdn}", nexmoNumber));
        // TODO handle release response
        String response = HttpUtil.defaultHttpPostHandler(nexmoReleaseUrl,
                new HashMap<String, String>(),
                BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
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

    public BalanceResponse checkBalance(Provider provider)
            throws ClientProtocolException, IOException {
        String nexoAccountBalanceurl = NEXMO_ACCOUNT_BALANCE_URL;
        nexoAccountBalanceurl = nexoAccountBalanceurl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey());
        BalanceResponse balanceResponse = new BalanceResponse();
        try {
            String response = HttpUtil
                    .defaultHttpGetHandler(nexoAccountBalanceurl,
                            BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                                    provider.getApiKey()))
                    .replace("value", "accountBalance");
            balanceResponse = ImiJsonUtil.deserialize(response,
                    BalanceResponse.class);
            if (balanceResponse.getAccountBalance() != null) {
                balanceResponse.setAccountBalance(
                        balanceResponse.getAccountBalance() + " EUR");
            }
        } catch (ImiException e) {
        }
        return balanceResponse;
    }

    @Override
    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, com.imi.rest.dao.model.Country country)
                    throws ClientProtocolException, IOException, ImiException {
        String nexmoPurchaseUrl = NEXMO_PURCHASE_URL;
        nexmoPurchaseUrl = nexmoPurchaseUrl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey())
                .replace("{country}", country.getCountryIso())
                .replace("{msisdn}", "" + number);
        String response = HttpUtil.defaultHttpPostHandler(nexmoPurchaseUrl,
                new HashMap<String, String>(),
                BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        // TODO need to map response accordingly
        return null;
    }

    public ApplicationResponse updateNumber(String number,
            String countryIsoCode, ApplicationResponse application,
            Provider provider)
                    throws ImiException, ClientProtocolException, IOException {
        String nexmoAccountUpdateUrl = NEXMO_ACCOUNT_UPDATE_URL;
        String nexmoNumber = number.trim() + countryIsoCode.trim();
        nexmoAccountUpdateUrl = nexmoAccountUpdateUrl
                .replace("{country}", countryIsoCode)
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey())
                .replace("{msisdn}", nexmoNumber);
        nexmoAccountUpdateUrl = getUpdatedUrl(nexmoAccountUpdateUrl,
                application);
        Map<String, String> requestBody = new HashMap<String, String>();
        String response = HttpUtil.defaultHttpPostHandler(nexmoAccountUpdateUrl,
                requestBody,
                BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                ContentType.APPLICATION_JSON.getMimeType());
        JSONObject nexmoResponse = XML.toJSONObject(response);
        ApplicationResponse applicationResponse = new ApplicationResponse();
        if (nexmoResponse.get("status").equals("200")) {
            applicationResponse.setMoHttpUrl(application.getMoHttpUrl());
            applicationResponse.setPhone_number(number);
            applicationResponse.setMoSmppSysType(application.getMoSmppSysType());
            applicationResponse.setVoiceCallbackType(application.getVoiceCallbackType());
            applicationResponse.setVoiceCallbackValue(application.getVoiceCallbackValue());
            applicationResponse.setStatusCallback(application.getStatusCallback());
        } else if (nexmoResponse.get("status").equals("420")) {
        	String message = "Number was not updated successfully, "
            		+ "Some Parameters to update Number were wrong "
            		+ "Please Check Whether appropriate values are being sent";
            ImiException e = new ImiException(message);
            throw e;
        }
        return applicationResponse;
    }

    public String getUpdatedUrl(String url,
            ApplicationResponse modifyapplication) {
        String toAppend = "?";
        if(modifyapplication.getMoHttpUrl() != null){
			toAppend.concat("moHttpUrl="+modifyapplication.getMoHttpUrl()+"&");
		}
		if(modifyapplication.getMoSmppSysType() != null){
			toAppend.concat("moSmppSysType="+modifyapplication.getMoSmppSysType()+"&");
		}
		if(modifyapplication.getVoiceCallbackType() != null){
			toAppend.concat("voiceCallbackType="+modifyapplication.getVoiceCallbackType()+"&");
		}
		if(modifyapplication.getVoiceCallbackValue() != null){
			toAppend.concat("voiceCallbackValue="+modifyapplication.getVoiceCallbackValue()+"&");
		}
		if(modifyapplication.getStatusCallback() != null){
			toAppend.concat("voiceStatusCallback="+modifyapplication.getStatusCallback()+"&");
		}
        toAppend = toAppend.substring(0, toAppend.length() - 1);
        url.concat(toAppend);
        return url;
    }

}
