package com.imi.rest.core.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
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
import com.imi.rest.dao.model.ResourceMaster;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidNumberException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.BalanceResponse;
import com.imi.rest.model.Country;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PlivoAccountResponse;
import com.imi.rest.model.PlivoPurchaseResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.CountrySearchService;
import com.imi.rest.service.ForexService;
import com.imi.rest.service.ResourceService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class PlivoFactoryImpl implements NumberSearch, CountrySearch,
        PurchaseNumber, UrlConstants, ProviderConstants, NumberTypeConstants {

    private static final String PLIVO_CSV_FILE_PATH = "/plivo_inbound_rates.csv";
    // max no of numbers to be obtained
    private static final Logger LOG = Logger
            .getLogger(CountrySearchService.class);
    private Double forexValue;
    @Autowired
    ForexService forexService;

    @Autowired
    ResourceService resourceService;

    @Override
    public void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String index,
            NumberResponse numberResponse)
                    throws ClientProtocolException, IOException, ImiException {
        List<Number> numberSearchList = numberResponse.getObjects() == null
                ? new ArrayList<Number>() : numberResponse.getObjects();
        String type = "any";
        if (numberType.equalsIgnoreCase(LANDLINE)) {
            type = "fixed";
        } else if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "mobile";
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "tollfree";
        } else if (numberType.equalsIgnoreCase(LOCAL)) {
            type = "fixed";
        }
        int offset = 0;
        if (!index.equalsIgnoreCase("FIRST")) {
            offset = Integer.parseInt(index);
        } else {
            Meta meta = numberResponse.getMeta() == null ? new Meta()
                    : numberResponse.getMeta();
            meta.setPreviousPlivoIndex("FIRST");
            numberResponse.setMeta(meta);
        }

        searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, type,
                pattern, numberSearchList, offset, numberResponse);
    }

    void searchPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            List<Number> numberSearchList, int offset,
            NumberResponse numberResponse)
                    throws ClientProtocolException, IOException, ImiException {
        String plivioPhoneSearchUrl = PLIVO_PHONE_SEARCH_URL;
        plivioPhoneSearchUrl = plivioPhoneSearchUrl
                .replace("{auth_id}", provider.getAuthId())
                .replace("{country_iso}", countryIsoCode)
                .replace("{type}", numberType)
                .replace("{services}", serviceTypeEnum.toString())
                .replace("{pattern}", "*" + pattern + "*");
        if (offset > 0) {
            plivioPhoneSearchUrl = plivioPhoneSearchUrl + "&offset=" + offset;
        }
        GenericRestResponse restResponse = null;
        restResponse = ImiHttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
                ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            NumberResponse numberResponseFromPlivo = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? ""
                            : restResponse.getResponseBody(),
                    NumberResponse.class);
            List<Number> plivioNumberList = numberResponseFromPlivo == null
                    ? new ArrayList<Number>()
                    : numberResponseFromPlivo.getObjects() == null
                            ? new ArrayList<Number>()
                            : numberResponseFromPlivo.getObjects();
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
            for (Number plivioNumber : plivioNumberList) {
                if (plivioNumber != null) {
                    boolean isValidNumber = true;
                    List<String> numberFeatures = new ArrayList<String>();
                    if (plivioNumber.isSmsEnabled()) {
                        numberFeatures.add(
                                ServiceConstants.SMS.toString().toUpperCase());
                    }
                    if (plivioNumber.isVoiceEnabled()) {
                        if (plivioNumber.isSmsEnabled()) {
                            numberFeatures.add(ServiceConstants.VOICE.toString()
                                    .toUpperCase());
                        }
                    }
                    for (String feature : numberFeatures) {
                        if (!expectedFeatures.contains(feature))
                            isValidNumber = false;
                    }
                    if (isValidNumber) {
                        plivioNumber.setProvider(PLIVO);
                        setServiceType(plivioNumber);
                        plivioNumber.setPriceUnit("USD");
                        String monthlyRentalRateInGBP = ImiDataFormatUtils
                                .forexConvert(forexValue,
                                        plivioNumber.getMonthlyRentalRate());
                        plivioNumber
                                .setMonthlyRentalRate(monthlyRentalRateInGBP);
                        String voiceRateInGBP = ImiDataFormatUtils.forexConvert(
                                forexValue, plivioNumber.getVoiceRate());
                        plivioNumber.setVoiceRate(voiceRateInGBP);
                        if (numberType.equalsIgnoreCase("fixed")
                                || numberType.equalsIgnoreCase("local")) {
                            numberType = "Landline";
                        }
                        plivioNumber.setType(numberType.toLowerCase());
                        numberSearchList.add(plivioNumber);
                    }
                }
            }
            if (numberResponseFromPlivo.getMeta() != null
                    && numberResponseFromPlivo.getMeta().getNext() != null
                    && !numberResponseFromPlivo.getMeta().getNext()
                            .equals("")) {
                Meta meta = numberResponse.getMeta() == null ? new Meta()
                        : numberResponse.getMeta();
                String previousPlivoIndex = meta.getNextPlivoIndex();
                previousPlivoIndex = "" + offset;
                String nextPlivoIndex = null;
                nextPlivoIndex = ""
                        + (numberResponseFromPlivo.getMeta().getOffset()
                                + numberResponseFromPlivo.getMeta().getLimit());
                meta.setPreviousPlivoIndex(previousPlivoIndex);
                meta.setNextPlivoIndex(nextPlivoIndex);
                numberResponse.setMeta(meta);
            }
            numberResponse.setObjects(numberSearchList);
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

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode)
                    throws ClientProtocolException, IOException, ImiException {
        Number numberDetails = getNumberDetails(number, provider);
        if (numberDetails == null) {
            throw new ImiException("Number requested for release " + number
                    + " is not present in your " + provider.getName()
                    + " account. ");
        }
        String plivioReleaseurl = PLIVO_RELEASE_URL;
        plivioReleaseurl = plivioReleaseurl.replace("{number}", number)
                .replace("{auth_id}", provider.getAuthId());
        GenericRestResponse response = ImiHttpUtil.defaultHttpDeleteHandler(
                plivioReleaseurl, new HashMap<String, String>(),
                ImiBasicAuthUtil.getBasicAuthHash(provider), null);
        if (response.getResponseCode() != HttpStatus.NO_CONTENT.value()) {
            throw new InvalidNumberException(number, provider.getName());
        }
    }

    public BalanceResponse checkBalance(Provider provider)
            throws ClientProtocolException, IOException {
        String plivoAccountBalanceurl = PLIVO_ACCOUNT_BALANCE_URL;
        plivoAccountBalanceurl = plivoAccountBalanceurl.replace("{auth_id}",
                provider.getAuthId());
        BalanceResponse balanceResponse = new BalanceResponse();
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                plivoAccountBalanceurl,
                ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            PlivoAccountResponse plivoAccountResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? ""
                            : restResponse.getResponseBody(),
                    PlivoAccountResponse.class);
            balanceResponse
                    .setAccountBalance(plivoAccountResponse.getCash_credits());
            if (balanceResponse.getAccountBalance() != null) {
                balanceResponse.setAccountBalance(
                        balanceResponse.getAccountBalance() + " USD");
            }
        }
        return balanceResponse;
    }

    @Override
    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, com.imi.rest.dao.model.Country country,
            ServiceConstants serviceTypeEnum)
                    throws ClientProtocolException, IOException, ImiException {
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        String plivoPurchaseUrl = PLIVO_PURCHASE_URL
                .replace("{auth_id}", provider.getAuthId())
                .replace("{number}", number);
        GenericRestResponse response = ImiHttpUtil.defaultHttpPostHandler(
                plivoPurchaseUrl, new HashMap<String, String>(),
                ImiBasicAuthUtil.getBasicAuthHash(provider),
                ContentType.APPLICATION_JSON.getMimeType());
        PlivoPurchaseResponse plivoPurchaseResponse = ImiJsonUtil.deserialize(
                response.getResponseBody() == null ? ""
                        : response.getResponseBody(),
                PlivoPurchaseResponse.class);
        if (response.getResponseCode() == HttpStatus.CREATED.value()) {
            if (plivoPurchaseResponse.getNumberStatus().get(0).getStatus()
                    .equalsIgnoreCase("pending")) {
                purchaseResponse.setAddressRequired(true);
            } else {
                purchaseResponse.setAddressRequired(false);
            }
            if (plivoPurchaseResponse.getStatus().equalsIgnoreCase("Success")) {
                purchaseResponse.setStatus("Success");
            }
            Number numberDetails = getNumberDetails(number, provider);
            ResourceMaster resourceMaster = resourceService
                    .updateResource(number, serviceTypeEnum);
            resourceService.updateResourceAllocation(resourceMaster);
            resourceService.updateChannelAssetsAllocation(resourceMaster);
            purchaseResponse.setNumber(number);
            purchaseResponse.setNumberType(numberType);
            if (numberDetails != null) {
                purchaseResponse.setMonthlyRentalRate(
                        numberDetails.getMonthlyRentalRate());
                purchaseResponse.setSetUpRate(numberDetails.getSetUpRate());
                purchaseResponse.setSmsRate(numberDetails.getSmsRate());
                purchaseResponse.setVoicePrice(numberDetails.getVoiceRate());
            }
            purchaseResponse
                    .setEffectiveDate(ImiDataFormatUtils.getCurrentTimeStamp());
            resourceService.updatePurchase(purchaseResponse, numberType,
                    ImiDataFormatUtils.getAddressRestrictions(
                            purchaseResponse.isAddressRequired(), null),
                    resourceMaster);
        } else {
            if (plivoPurchaseResponse.getError() != null) {
                throw new ImiException("error occured for number provided."
                        + number + " Error :"
                        + plivoPurchaseResponse.getError());
            }
        }
        return purchaseResponse;
    }

    private Number getNumberDetails(String number, Provider provider)
            throws ClientProtocolException, IOException {
        String plivoAccountNumberUrl = PLIVO_RELEASE_URL
                .replace("{auth_id}", provider.getAuthId())
                .replace("{number}", number);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                plivoAccountNumberUrl,
                ImiBasicAuthUtil.getBasicAuthHash(provider));
        Number numberDetails = null;
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            numberDetails = ImiJsonUtil
                    .deserialize(restResponse.getResponseBody(), Number.class);
        }
        return numberDetails;
    }

    public ApplicationResponse createApplication(
            ApplicationResponse plivoApplication, Provider provider)
                    throws ImiException {
        String plivoCreateApplicationurl = PLIVO_APPLICATION_CREATE_URL;
        plivoCreateApplicationurl = plivoCreateApplicationurl
                .replace("{auth_id}", provider.getAuthId());
        ApplicationResponse plivoApplicationResponse = new ApplicationResponse();
        try {
            Map<String, String> requestBody = new HashMap<String, String>();
            if (plivoApplication.getApp_name() != null
                    && plivoApplication.getVoiceUrl() != null) {
                requestBody.put("app_name", plivoApplication.getApp_name());
                requestBody.put("answer_url", plivoApplication.getVoiceUrl());
            } else {
                throw new ImiException(
                        "app_name and answer_url field is compulsory to create a new application");
            }
            if (plivoApplication.getVoiceMethod() != null) {
                requestBody.put("answer_method",
                        plivoApplication.getVoiceMethod());
            }
            if (plivoApplication.getStatusCallback() != null) {
                requestBody.put("hangup_url",
                        plivoApplication.getStatusCallback());
            }
            if (plivoApplication.getStatusCallbackMethod() != null) {
                requestBody.put("hangup_method",
                        plivoApplication.getStatusCallbackMethod());
            }
            if (plivoApplication.getVoiceFallback() != null) {
                requestBody.put("fallback_answer_url",
                        plivoApplication.getVoiceFallback());
            }
            if (plivoApplication.getVoiceFallbackMethod() != null) {
                requestBody.put("fallback_method",
                        plivoApplication.getVoiceFallbackMethod());
            }
            if (plivoApplication.getSmsUrl() != null) {
                requestBody.put("message_url", plivoApplication.getSmsUrl());
            }
            if (plivoApplication.getSmsMethod() != null) {
                requestBody.put("message_method",
                        plivoApplication.getSmsMethod());
            }
            if (plivoApplication.getDefault_number_app() != null) {
                requestBody.put("default_number_app",
                        plivoApplication.getDefault_number_app().toString());
            }
            if (plivoApplication.getDefault_endpoint_app() != null) {
                requestBody.put("default_endpoint_app",
                        plivoApplication.getDefault_endpoint_app().toString());
            }
            GenericRestResponse restResponse = ImiHttpUtil
                    .defaultHttpPostHandler(plivoCreateApplicationurl,
                            requestBody,
                            ImiBasicAuthUtil.getBasicAuthHash(provider),
                            ContentType.APPLICATION_JSON.getMimeType());
            PlivoPurchaseResponse plivoResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? ""
                            : restResponse.getResponseBody(),
                    PlivoPurchaseResponse.class);
            if (restResponse.getResponseCode() == HttpStatus.CREATED.value()) {
                if (plivoResponse.getMessage().equals("created")) {
                    plivoApplication = getApplication(plivoResponse.getApp_id(),
                            provider);
                } else {
                    throw new ImiException(plivoResponse.getMessage());
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        return plivoApplicationResponse;
    }

    public ApplicationResponse updateApplication(
            ApplicationResponse plivoApplication, Provider provider)
                    throws ClientProtocolException, IOException, ImiException {
        String plivoApplicationUpdateUrl = PLIVO_APPLICATION_UPDATE_URL;
        if (plivoApplication.getApp_id() == null) {
            throw new ImiException(
                    "app_id field must be present in sent parameters to update an application");
        }
        plivoApplicationUpdateUrl = plivoApplicationUpdateUrl
                .replace("{auth_id}", provider.getAuthId())
                .replace("{app_id}", plivoApplication.getApp_id());
        ApplicationResponse currentPlivoApplication = getApplication(
                plivoApplication.getApp_id(), provider);
        Map<String, String> requestBody = new HashMap<String, String>();
        if (plivoApplication.getVoiceUrl() != null && plivoApplication
                .getVoiceUrl() != currentPlivoApplication.getVoiceUrl()) {
            requestBody.put("answer_url", plivoApplication.getVoiceUrl());
        }
        if (plivoApplication.getApp_name() != null && plivoApplication
                .getApp_name() != currentPlivoApplication.getApp_name()) {
            requestBody.put("app_name", plivoApplication.getApp_name());
        }
        if (plivoApplication.getVoiceMethod() != null && plivoApplication
                .getVoiceMethod() != currentPlivoApplication.getVoiceMethod()) {
            requestBody.put("answer_method", plivoApplication.getVoiceMethod());
        }
        if (plivoApplication.getStatusCallback() != null) {
            requestBody.put("hangup_url", plivoApplication.getStatusCallback());
        }
        if (plivoApplication.getStatusCallbackMethod() != null) {
            requestBody.put("hangup_method",
                    plivoApplication.getStatusCallbackMethod());
        }
        if (plivoApplication.getVoiceFallback() != null) {
            requestBody.put("fallback_answer_url",
                    plivoApplication.getVoiceFallback());
        }
        if (plivoApplication.getVoiceFallbackMethod() != null) {
            requestBody.put("fallback_method",
                    plivoApplication.getVoiceFallbackMethod());
        }
        if (plivoApplication.getSmsUrl() != null) {
            requestBody.put("message_url", plivoApplication.getSmsUrl());
        }
        if (plivoApplication.getSmsMethod() != null) {
            requestBody.put("message_method", plivoApplication.getSmsMethod());
        }
        if (plivoApplication.getDefault_number_app() != null) {
            requestBody.put("default_number_app",
                    plivoApplication.getDefault_number_app().toString());
        }
        if (plivoApplication.getDefault_endpoint_app() != null) {
            requestBody.put("default_endpoint_app",
                    plivoApplication.getDefault_endpoint_app().toString());
        }
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                plivoApplicationUpdateUrl, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(provider),
                ContentType.APPLICATION_JSON.getMimeType());
        PlivoPurchaseResponse plivoResponse = ImiJsonUtil.deserialize(
                restResponse.getResponseBody() == null ? ""
                        : restResponse.getResponseBody(),
                PlivoPurchaseResponse.class);
        if (restResponse.getResponseCode() == HttpStatus.ACCEPTED.value()) {
            if (plivoResponse.getMessage().equals("changed")) {
                plivoApplication = getApplication(
                        currentPlivoApplication.getApp_id(), provider);
            } else {
                throw new ImiException(plivoResponse.getMessage());
            }
        }
        return plivoApplication;
    }

    public ApplicationResponse getApplication(String appId, Provider provider)
            throws ClientProtocolException, IOException, ImiException {
        String plivoApplicationUpdateUrl = PLIVO_APPLICATION_UPDATE_URL;
        plivoApplicationUpdateUrl = plivoApplicationUpdateUrl
                .replace("{auth_id}", provider.getAuthId())
                .replace("{app_id}", appId);
        ApplicationResponse plivoApplicationResponse = null;
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                plivoApplicationUpdateUrl,
                ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            plivoApplicationResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? ""
                            : restResponse.getResponseBody(),
                    ApplicationResponse.class);
        }
        return plivoApplicationResponse;
    }

    public ApplicationResponse updateNumber(String number,
            ApplicationResponse applicationResponsetoModify, Provider provider)
                    throws ClientProtocolException, IOException, ImiException {
        String plivoNumberUpdateUrl = PLIVO_RELEASE_URL;
        plivoNumberUpdateUrl = plivoNumberUpdateUrl.replace("{number}", number)
                .replace("{auth_id}", provider.getAuthId());
        PlivoPurchaseResponse plivioPurchaseResponse = new PlivoPurchaseResponse();
        ApplicationResponse plivoApplicationResponse = getApplicationByNumber(
                number, provider);
        if (plivoApplicationResponse != null) {
            applicationResponsetoModify
                    .setApi_id(plivoApplicationResponse.getApi_id());
            applicationResponsetoModify
                    .setApp_id(plivoApplicationResponse.getApp_id());
            plivoApplicationResponse = updateApplication(
                    applicationResponsetoModify, provider);
        } else {
            plivoApplicationResponse = createApplication(
                    applicationResponsetoModify, provider);
        }
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("app_id", plivoApplicationResponse.getApp_id());
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                plivoNumberUpdateUrl, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(provider),
                ContentType.APPLICATION_JSON.getMimeType());
        if (restResponse.getResponseCode() == HttpStatus.ACCEPTED.value()) {
            plivioPurchaseResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? ""
                            : restResponse.getResponseBody(),
                    PlivoPurchaseResponse.class);
            if (!plivioPurchaseResponse.getMessage().equals("changed")) {
                String message = "application created successfully, but not assigned to the upadted number";
                ImiException e = new ImiException(message);
                throw e;
            }
        }
        return plivoApplicationResponse;
    }

    public ApplicationResponse updateAllNumbers(
            ApplicationResponse applicationResponsetoModify, Provider provider)
                    throws ImiException, ClientProtocolException, IOException {
        NumberResponse numberResponse = getAllRentedNumbers(provider);
        ApplicationResponse applicationResponse = new ApplicationResponse();
        if (numberResponse == null || numberResponse.getObjects().size() == 0) {
            String message = "No numbers rented to this Account to update.";
            ImiException e = new ImiException(message);
            throw e;
        } else {
            List<Number> rentedNumbersList = numberResponse.getObjects();
            List<String> errorNumber = new ArrayList<String>();
            for (Number numberObj : rentedNumbersList) {
                try {
                    updateNumber(numberObj.getNumber(),
                            applicationResponsetoModify, provider);
                } catch (Exception e) {
                    errorNumber.add(numberObj.getNumber());
                }
            }
            if (errorNumber.size() > 0) {
                String message = errorNumber.size() + " out of "
                        + rentedNumbersList.size()
                        + " numbers were not updated, following list contains numbers, Which were not Updated: {";
                for (String num : errorNumber) {
                    message.concat(" " + num + ",");
                }
                message = message.substring(0, message.length() - 1);
                message.concat("}. ");
                ImiException e = new ImiException(message);
                throw e;
            } else {
                applicationResponse = getApplicationByNumber(
                        rentedNumbersList.get(0).getNumber(), provider);
            }
        }
        return applicationResponse;
    }

    private ApplicationResponse getApplicationByNumber(String number,
            Provider provider)
                    throws ClientProtocolException, IOException, ImiException {
        String plivoNumberGetUrl = PLIVO_RELEASE_URL;
        plivoNumberGetUrl = plivoNumberGetUrl.replace("{number}", number)
                .replace("{auth_id}", provider.getAuthId());
        ApplicationResponse plivoApplicationResponse = null;
        GenericRestResponse response = ImiHttpUtil.defaultHttpGetHandler(
                plivoNumberGetUrl, ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (response.getResponseCode() == HttpStatus.OK.value()) {
            Number numberObj = ImiJsonUtil
                    .deserialize(response.getResponseBody(), Number.class);
            String app_id = numberObj.getApplication()
                    .substring(
                            numberObj.getApplication()
                                    .lastIndexOf("Application/") + 12,
                            numberObj.getApplication().length() - 1)
                    .trim();
            if (app_id.length() > 0) {
                plivoApplicationResponse = getApplication(app_id, provider);
            }
        }

        return plivoApplicationResponse;
    }

    private NumberResponse getAllRentedNumbers(Provider provider)
            throws ClientProtocolException, IOException {
        String plivoNumberSGetUrl = PLIVO_ALL_NUMBERS_URL;
        NumberResponse numberResponse = new NumberResponse();
        GenericRestResponse response = ImiHttpUtil.defaultHttpGetHandler(
                plivoNumberSGetUrl,
                ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (response.getResponseCode() == HttpStatus.OK.value()) {
            numberResponse = ImiJsonUtil.deserialize(response.getResponseBody(),
                    NumberResponse.class);
        }
        return numberResponse;
    }
}
