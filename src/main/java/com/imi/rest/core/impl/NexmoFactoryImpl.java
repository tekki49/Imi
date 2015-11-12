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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.constants.NumberTypeConstants;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.CountrySearch;
import com.imi.rest.core.NumberSearch;
import com.imi.rest.core.PurchaseNumber;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidNumberException;
import com.imi.rest.exception.InvalidProviderException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.BalanceResponse;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.NexmoPurchaseResponse;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.ForexService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class NexmoFactoryImpl implements NumberSearch, CountrySearch,
        PurchaseNumber, UrlConstants, ProviderConstants, NumberTypeConstants {

    private static final String NEXMO_PRICING_FILE_PATH = "/nexmo_pricing.xls";
    private Double forexValue;
    private static Map<String, CountryPricing> countryPricingMap;
    private final static int THRESHOLD = 20;

    @Autowired
    ForexService forexService;

    @Override
    public void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String nexmoIndex,
            NumberResponse numberResponse)
                    throws ClientProtocolException, IOException, ImiException {
        List<Number> numberSearchList = new ArrayList<Number>();
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
                pattern, numberSearchList, index, numberResponse, "FIRST");
        List<Number> numberList = numberResponse.getObjects() == null
                ? new ArrayList<Number>() : numberResponse.getObjects();
        numberList.addAll(numberSearchList);
    }

    void searchPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            List<Number> numberSearchList, int index,
            NumberResponse numberResponse, String previousNexmoIndex)
                    throws ClientProtocolException, IOException, ImiException {
        String nexmoPhoneSearchUrl = NEXMO_PHONE_SEARCH_URL;
        nexmoPhoneSearchUrl = nexmoPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey())
                .replace("{pattern}", pattern)
                .replace("{features}", serviceTypeEnum.toString().toUpperCase())
                .replace("{index}", "" + index);
        GenericRestResponse restReponse = null;
        try {
            restReponse = ImiHttpUtil
                    .defaultHttpGetHandler(nexmoPhoneSearchUrl);
        } catch (Exception e) {
        }
        if (restReponse != null
                && restReponse.getResponseCode() == HttpStatus.OK.value()) {
            NumberResponse nexmoNumberResponse = ImiJsonUtil.deserialize(
                    restReponse.getResponseBody() == null ? ""
                            : restReponse.getResponseBody(),
                    NumberResponse.class);
            if (nexmoNumberResponse == null)
                return;
            List<Number> nexmoNumberList = nexmoNumberResponse == null
                    ? new ArrayList<Number>()
                    : nexmoNumberResponse.getObjects() == null
                            ? new ArrayList<Number>()
                            : nexmoNumberResponse.getObjects();
            if (countryPricingMap == null) {
                getNexmoPricing(NEXMO_PRICING_FILE_PATH);
            }
            if (forexValue == null) {
                forexValue = forexService.getForexValueByName("USD_GBP")
                        .getValue();
            }
            List<String> expectedFeatures = new ArrayList<String>();
            if (serviceTypeEnum.equals(ServiceConstants.VOICE)) {
                expectedFeatures
                        .add(ServiceConstants.VOICE.toString().toUpperCase());
            } else if (serviceTypeEnum.equals(ServiceConstants.SMS)) {
                expectedFeatures
                        .add(ServiceConstants.SMS.toString().toUpperCase());
            } else if (serviceTypeEnum.equals(ServiceConstants.BOTH)) {
                expectedFeatures
                        .add(ServiceConstants.SMS.toString().toUpperCase());
                expectedFeatures
                        .add(ServiceConstants.VOICE.toString().toUpperCase());
            }
            if (expectedFeatures.size() > 1) {
                return;
            }
            for (Number nexmoNumber : nexmoNumberList) {
                if (nexmoNumber != null) {
                    boolean isValidNumber = true;
                    if (nexmoNumber.getFeatures() == null) {
                        isValidNumber = false;
                    }
                    for (String feature : nexmoNumber.getFeatures()) {
                        if (!expectedFeatures.contains(feature))
                            isValidNumber = false;
                    }
                    if (isValidNumber) {
                        setServiceType(nexmoNumber);
                        CountryPricing countryPricing = countryPricingMap
                                .get(nexmoNumber.getCountry().toUpperCase());
                        nexmoNumber.setType(numberType);
                        nexmoNumber.setPriceUnit("EUR");
                        nexmoNumber.setMonthlyRentalRate(
                                ImiDataFormatUtils.forexConvert(forexValue,
                                        countryPricing.getPricing()
                                                .get(numberType)
                                                .get("monthlyRateInEuros")));
                        if (numberType.equalsIgnoreCase("landline")
                                || numberType.equalsIgnoreCase("tollfree")) {
                            nexmoNumber.setVoiceRate(
                                    ImiDataFormatUtils.forexConvert(forexValue,
                                            countryPricing.getPricing().get(
                                                    numberType)
                                            .get("inboundRateInEuros")));
                        } else if (numberType.equalsIgnoreCase("mobile")) {
                            nexmoNumber.setSmsRate(
                                    ImiDataFormatUtils.forexConvert(forexValue,
                                            countryPricing.getPricing().get(
                                                    numberType)
                                            .get("inboundRateInEuros")));
                        }
                        nexmoNumber.setCountry(countryPricing.getCountry());
                        nexmoNumber.setProvider(NEXMO);
                        numberSearchList.add(nexmoNumber);
                    }
                }
            }
            if (nexmoNumberResponse.getCount() - index * 100 > 0) {
                if (numberSearchList.size() < THRESHOLD) {
                    searchPhoneNumbers(provider, serviceTypeEnum,
                            countryIsoCode, numberType, pattern,
                            numberSearchList, (index + 1), nexmoNumberResponse,
                            previousNexmoIndex);
                } else {
                    Meta meta = numberResponse.getMeta() == null ? new Meta()
                            : numberResponse.getMeta();
                    String nextNexmoIndex = "" + (index + 1);
                    meta.setPreviousNexmoIndex(previousNexmoIndex);
                    meta.setNextNexmoIndex(nextNexmoIndex);
                    numberResponse.setMeta(meta);
                }
            }
        }
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
            String countryIsoCode)
                    throws ClientProtocolException, IOException, ImiException {
        String nexmoReleaseUrl = NEXMO_RELEASE_URL;
        String nexmoNumber = number.trim() + countryIsoCode.trim();
        nexmoReleaseUrl = nexmoReleaseUrl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}",
                        provider.getApiKey().replace("{country}", "")
                                .replace("{msisdn}", nexmoNumber));
        // TODO handle release response
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                nexmoReleaseUrl, new HashMap<String, String>(),
                ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {

        } else if (restResponse.getResponseCode() == HttpStatus.UNAUTHORIZED
                .value()) {
            throw new InvalidProviderException(provider.getName());

        } else if (restResponse.getResponseCode() == HttpStatus.METHOD_FAILURE
                .value()) {
            throw new InvalidNumberException(number, provider.getName());
        }
    }

    public void getNexmoPricing(String fileName) throws IOException {
        InputStream in = getClass().getResourceAsStream(fileName);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        HSSFSheet voicePriceSheet = workbook.getSheetAt(3);
        countryPricingMap = new HashMap<String, CountryPricing>();
        // voice pricing
        for (int i = 1; i < voicePriceSheet.getLastRowNum(); i++) {
            CountryPricing countryPricing = new CountryPricing();
            Row row = voicePriceSheet.getRow(i);
            countryPricing
                    .setCountry(row.getCell(0).toString().split(" - ")[1]);
            countryPricing.setCountryIsoCode(
                    row.getCell(0).toString().split(" - ")[0]);
            Map<String, String> priceMap = new HashMap<String, String>();
            Map<String, Map<String, String>> pricing = new HashMap<String, Map<String, String>>();
            priceMap.put("monthlyRateInEuros", row.getCell(1).toString());
            priceMap.put("inboundRateInEuros", row.getCell(2).toString());
            pricing.put("landline", priceMap);
            priceMap = new HashMap<String, String>();
            priceMap.put("monthlyRateInEuros", row.getCell(3).toString());
            priceMap.put("inboundRateInEuros", row.getCell(4).toString());
            pricing.put("tollfree", priceMap);
            countryPricing.setPricing(pricing);
            countryPricingMap.put(countryPricing.getCountryIsoCode(),
                    countryPricing);
        }
        // sms pricing
        HSSFSheet smsPriceSheet = workbook.getSheetAt(2);
        for (int i = 1; i < smsPriceSheet.getLastRowNum(); i++) {
            Row row = smsPriceSheet.getRow(i);
            CountryPricing countryPricing = countryPricingMap
                    .get(row.getCell(0).toString().split(" - ")[0]);
            if (countryPricing == null) {
                countryPricing = new CountryPricing();
                countryPricing
                        .setCountry(row.getCell(0).toString().split(" - ")[1]);
                countryPricing.setCountryIsoCode(
                        row.getCell(0).toString().split(" - ")[0]);
            }
            Map<String, String> priceMap = new HashMap<String, String>();
            Map<String, Map<String, String>> pricing = countryPricing
                    .getPricing();
            priceMap.put("monthlyRateInEuros", row.getCell(1).toString());
            priceMap.put("inboundRateInEuros", row.getCell(2).toString());
            pricing.put("mobile", priceMap);
            countryPricing.setPricing(pricing);
        }
        in.close();
        workbook.close();
    }

    public BalanceResponse checkBalance(Provider provider)
            throws ClientProtocolException, IOException, ImiException {
        String nexoAccountBalanceurl = NEXMO_ACCOUNT_BALANCE_URL;
        nexoAccountBalanceurl = nexoAccountBalanceurl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey());
        BalanceResponse balanceResponse = new BalanceResponse();
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                nexoAccountBalanceurl, ImiBasicAuthUtil.getBasicAuthHash(
                        provider.getAuthId(), provider.getApiKey()));
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            balanceResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody(), BalanceResponse.class);
        }
        if (balanceResponse.getAccountBalance() != null) {
            balanceResponse.setAccountBalance(
                    balanceResponse.getAccountBalance() + " EUR");
        }
        return balanceResponse;
    }

    @Override
    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, com.imi.rest.dao.model.Country country,
            ServiceConstants serviceTypeEnum)
                    throws ClientProtocolException, IOException, ImiException {
        String nexmoPurchaseUrl = NEXMO_PURCHASE_URL;
        nexmoPurchaseUrl = nexmoPurchaseUrl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey())
                .replace("{country}", country.getCountryIso())
                .replace("{msisdn}", "" + number);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                nexmoPurchaseUrl, new HashMap<String, String>(),
                ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        NexmoPurchaseResponse nexmoPurchaseResponse = ImiJsonUtil.deserialize(
                restResponse.getResponseBody(), NexmoPurchaseResponse.class);
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            if (nexmoPurchaseResponse.getErrorcode().equals("200")
                    && nexmoPurchaseResponse.getErrorCodeLabel()
                            .equals("success")) {
                return purchaseResponse;
            }
        } else if (restResponse.getResponseCode() == HttpStatus.UNAUTHORIZED
                .value()) {
            throw new InvalidProviderException(provider.getName());

        } else if (restResponse.getResponseCode() == HttpStatus.METHOD_FAILURE
                .value()) {
            if (nexmoPurchaseResponse.getErrorcode().equals("420")
                    && nexmoPurchaseResponse.getErrorCodeLabel()
                            .equals("method failed")) {
                throw new InvalidNumberException(number, provider.getName());
            }
        }
        throw new ImiException("Some Error occured while purchasing the number "
                + number + " please try again");
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
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                nexmoAccountUpdateUrl, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                ContentType.APPLICATION_JSON.getMimeType());
        ApplicationResponse applicationResponse = new ApplicationResponse();
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            NexmoPurchaseResponse nexmoResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody(),
                    NexmoPurchaseResponse.class);
            if (nexmoResponse.getErrorcode().equals("200")) {
                applicationResponse.setMoHttpUrl(application.getMoHttpUrl());
                applicationResponse.setPhone_number(number);
                applicationResponse
                        .setMoSmppSysType(application.getMoSmppSysType());
                applicationResponse.setVoiceCallbackType(
                        application.getVoiceCallbackType());
                applicationResponse.setVoiceCallbackValue(
                        application.getVoiceCallbackValue());
                applicationResponse
                        .setStatusCallback(application.getStatusCallback());
            } else if (nexmoResponse.getErrorcode().equals("420")) {
                String message = "Number was not updated successfully, "
                        + "Some Parameters to update Number were wrong "
                        + "Please Check Whether appropriate values are being sent";
                throw new ImiException(message);
            }
        }
        return applicationResponse;
    }

    public String getUpdatedUrl(String url,
            ApplicationResponse modifyapplication) {
        String toAppend = "?";
        if (modifyapplication.getMoHttpUrl() != null) {
            toAppend.concat(
                    "moHttpUrl=" + modifyapplication.getMoHttpUrl() + "&");
        }
        if (modifyapplication.getMoSmppSysType() != null) {
            toAppend.concat("moSmppSysType="
                    + modifyapplication.getMoSmppSysType() + "&");
        }
        if (modifyapplication.getVoiceCallbackType() != null) {
            toAppend.concat("voiceCallbackType="
                    + modifyapplication.getVoiceCallbackType() + "&");
        }
        if (modifyapplication.getVoiceCallbackValue() != null) {
            toAppend.concat("voiceCallbackValue="
                    + modifyapplication.getVoiceCallbackValue() + "&");
        }
        if (modifyapplication.getStatusCallback() != null) {
            toAppend.concat("voiceStatusCallback="
                    + modifyapplication.getStatusCallback() + "&");
        }
        toAppend = toAppend.substring(0, toAppend.length() - 1);
        url.concat(toAppend);
        return url;
    }

}
