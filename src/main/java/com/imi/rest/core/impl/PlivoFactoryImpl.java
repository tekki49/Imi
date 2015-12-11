package com.imi.rest.core.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.imi.rest.core.aop.ImportCountryAop;
import com.imi.rest.core.aop.NumberSearchAop;
import com.imi.rest.core.aop.PricingAop;
import com.imi.rest.core.aop.PurchaseAop;
import com.imi.rest.core.aop.ReleaseAop;
import com.imi.rest.core.aop.UpdateNumberAop;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.BalanceResponse;
import com.imi.rest.model.Country;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PlivoAccountResponse;
import com.imi.rest.model.PlivoPurchaseResponse;
import com.imi.rest.model.PurchaseRequest;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.ForexService;
import com.imi.rest.service.ResourceService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class PlivoFactoryImpl implements NumberSearch, CountrySearch,
        PurchaseNumber, UrlConstants, ProviderConstants, NumberTypeConstants {

    boolean test = false;

    private static final Logger LOG = Logger.getLogger(PlivoFactoryImpl.class);

    private Double forexValue;

    @Autowired
    ForexService forexService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    NumberSearchAop numberSearchAop;

    @Autowired
    PricingAop pricingAop;

    @Autowired
    ImportCountryAop importCountryAop;

    @Autowired
    ReleaseAop releaseAop;

    @Autowired
    PurchaseAop purchaseAop;

    @Autowired
    UpdateNumberAop updateNumberAop;

    @Override
    public void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String index,
            NumberResponse numberResponse, String markup)
                    throws ClientProtocolException, IOException {
        String type = "any";
        if (numberType.equalsIgnoreCase(LANDLINE)
                || numberType.equalsIgnoreCase(LOCAL)) {
            type = "fixed";
            numberType = LANDLINE;
        } else if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "mobile";
            numberType = MOBILE;
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "tollfree";
            numberType = TOLLFREE;
        } else if (numberType.equalsIgnoreCase(MULTI)) {
            type = "any";
            numberType = MULTI;
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
        List<Number> numberSearchList = new ArrayList<Number>();
        forexValue = forexService.getForexValueByName("USD_GBP").getValue();
        numberSearchAop.searchPlivoPhoneNumbers(provider, serviceTypeEnum,
                countryIsoCode, type, pattern, numberSearchList, offset,
                numberResponse, forexValue, numberType, markup);
        List<Number> numberList = numberResponse.getObjects() == null
                ? new ArrayList<Number>() : numberResponse.getObjects();
        numberList.addAll(numberSearchList);
        numberResponse.setObjects(numberList);
    }

    @Override
    public Set<Country> importCountries(
            Map<String, Map<String, String>> providerCapabilities)
                    throws FileNotFoundException, JsonParseException,
                    JsonMappingException, IOException {
        return importCountryAop.importPlivoCountries(providerCapabilities);
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey) throws ClientProtocolException, IOException {
        String plivoNumber = (number.trim().replace("+", ""));
        Number numberDetails = getNumberDetails(plivoNumber, provider);
        releaseAop.releasePlivoNumber(plivoNumber, provider, countryIsoCode,
                numberDetails, clientkey);
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
            ApplicationResponse plivoApplication, Provider provider) {
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
                String message = "app_name and answer_url field is compulsory to create a new application";
                LOG.error(message);
                throw InboundRestException.createApiException(
                        InboundApiErrorCodes.PLIVO_APPLICATION_CREATION_EXCEPTION,
                        message);
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
                plivoApplication = getApplication(plivoResponse.getApp_id(),
                        provider);
            } else {
                String message = "Exception occured while creating a Plivo application. response from Plivo is "
                        + restResponse.getResponseBody();
                LOG.error(message);
                throw InboundRestException.createApiException(
                        InboundApiErrorCodes.PLIVO_APPLICATION_CREATION_EXCEPTION,
                        message);
            }
        } catch (ClientProtocolException e) {
            LOG.error(ImiDataFormatUtils.getStackTrace(e));
        } catch (IOException e) {
            LOG.error(ImiDataFormatUtils.getStackTrace(e));
        }
        ;
        return plivoApplicationResponse;
    }

    public ApplicationResponse updateApplication(
            ApplicationResponse plivoApplication, Provider provider)
                    throws ClientProtocolException, IOException {
        String plivoApplicationUpdateUrl = PLIVO_APPLICATION_UPDATE_URL;
        if (plivoApplication.getApp_id() == null) {
            String message = "app_id field must be present in sent parameters to update an application";
            LOG.error(message);
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.PLIVO_APPLICATION_UPDATE_EXCEPTION,
                    message);
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
            plivoApplication = getApplication(
                    currentPlivoApplication.getApp_id(), provider);
        } else {
            String message = "Exception while updating the application. Response from Plivo is "
                    + restResponse.getResponseBody();
            LOG.error(message);
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.PLIVO_APPLICATION_UPDATE_EXCEPTION,
                    message);
        }
        return plivoApplication;
    }

    public ApplicationResponse getApplication(String appId, Provider provider)
            throws ClientProtocolException, IOException {
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
        } else {
            String message = "Exception while getting the application details. Response from Plivo is "
                    + restResponse.getResponseBody();
            LOG.error(message);
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.PLIVO_APPLICATION_DETAILS_EXCEPTION,
                    message);
        }
        return plivoApplicationResponse;
    }

    public ApplicationResponse updateNumber(String number,
            ApplicationResponse applicationResponsetoModify, Provider provider,
            Integer userid, Integer clientId, Integer groupid, Integer teamid,
            String clientname, String clientkey)
                    throws ClientProtocolException, IOException {
        if (test)
            return new ApplicationResponse();
        String plivoNumberUpdateUrl = PLIVO_RELEASE_URL;
        String plivoNumber = (number.trim().replace("+", ""));
        plivoNumberUpdateUrl = plivoNumberUpdateUrl
                .replace("{number}", plivoNumber)
                .replace("{auth_id}", provider.getAuthId());
        PlivoPurchaseResponse plivioPurchaseResponse = new PlivoPurchaseResponse();
        ApplicationResponse plivoApplicationResponse = new ApplicationResponse();
        if (applicationResponsetoModify.getApp_id() == null) {
            plivoApplicationResponse = getApplicationByNumber(plivoNumber,
                    provider);
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
        } else {
            plivoApplicationResponse
                    .setApp_id(applicationResponsetoModify.getApp_id());
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
            plivoApplicationResponse.setVoiceUrl(applicationResponsetoModify.getApp_id());
            plivoApplicationResponse.setSmsUrl(applicationResponsetoModify.getApp_id());
            resourceService.provisionData(plivoNumber,
                    plivoApplicationResponse);
        } else {
            String message = "Exception while updating the number " + number
                    + " . Response from Plivo is "
                    + restResponse.getResponseBody();
            LOG.error(message);
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.NUMBER_PROVIDER_ASSOCIATION_EXCEPTION,
                    message);
        }
        return plivoApplicationResponse;
    }

    public ApplicationResponse updateAllNumbers(
            ApplicationResponse applicationResponsetoModify, Provider provider,
            Integer userid, Integer clientId, Integer groupid, Integer teamid,
            String clientname, String clientkey)
                    throws ClientProtocolException, IOException {
        NumberResponse numberResponse = getAllRentedNumbers(provider);
        ApplicationResponse applicationResponse = new ApplicationResponse();
        if (numberResponse == null || numberResponse.getObjects().size() == 0) {
            String message = "No numbers rented to this Account to update.";
            LOG.error(message);
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.NUMBER_PROVIDER_ASSOCIATION_EXCEPTION,
                    message);
        } else {
            List<Number> rentedNumbersList = numberResponse.getObjects();
            List<String> errorNumber = new ArrayList<String>();
            for (Number numberObj : rentedNumbersList) {
                try {
                    updateNumber(numberObj.getNumber(),
                            applicationResponsetoModify, provider, userid,
                            clientId, groupid, teamid, clientname, clientkey);
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
                // throw new ImiException(message);
            } else {
                applicationResponse = getApplicationByNumber(
                        rentedNumbersList.get(0).getNumber(), provider);
            }
        }
        return applicationResponse;
    }

    private ApplicationResponse getApplicationByNumber(String number,
            Provider provider) throws ClientProtocolException, IOException {
        String plivoNumberGetUrl = PLIVO_RELEASE_URL;
        plivoNumberGetUrl = plivoNumberGetUrl.replace("{number}", number)
                .replace("{auth_id}", provider.getAuthId());
        ApplicationResponse plivoApplicationResponse = null;
        GenericRestResponse response = ImiHttpUtil.defaultHttpGetHandler(
                plivoNumberGetUrl, ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (response.getResponseCode() == HttpStatus.OK.value()) {
            Number numberObj = ImiJsonUtil
                    .deserialize(response.getResponseBody(), Number.class);
            String appId = numberObj.getApplication()
                    .substring(
                            numberObj.getApplication()
                                    .lastIndexOf("Application/") + 12,
                            numberObj.getApplication().length() - 1)
                    .trim();
            if (appId.length() > 0) {
                plivoApplicationResponse = getApplication(appId, provider);
            }
        }else
        {
            String message = "Number "+number +" is not available in your "+provider.getName() +" account";
            LOG.error(message);
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.NUMBER_PROVIDER_ASSOCIATION_EXCEPTION,
                    message);
        }
        return plivoApplicationResponse;
    }

    private NumberResponse getAllRentedNumbers(Provider provider)
            throws ClientProtocolException, IOException {
        String offset = "0";
        String plivoNumberSGetUrl = PLIVO_ALL_NUMBERS_URL;
        NumberResponse numberResponse = new NumberResponse();
        List<Number> purchasedNumberList = new ArrayList<Number>();
        boolean rescursive = true;
        while (rescursive) {
            String url = plivoNumberSGetUrl.replace("{offset}", offset);
            GenericRestResponse response = ImiHttpUtil.defaultHttpGetHandler(
                    url, ImiBasicAuthUtil.getBasicAuthHash(provider));
            NumberResponse numberResponse2 = null;
            if (response.getResponseCode() == HttpStatus.OK.value()) {
                numberResponse2 = ImiJsonUtil.deserialize(
                        response.getResponseBody(), NumberResponse.class);
            }
            if (numberResponse2 == null || numberResponse2.getMeta() == null
                    || numberResponse2.getMeta().getNext() == null) {
                rescursive = false;
            } else {
                purchasedNumberList.addAll(numberResponse2.getObjects());
                offset = numberResponse2.getMeta().getNext();
            }
        }
        numberResponse.setObjects(purchasedNumberList);
        return numberResponse;
    }

    @Override
    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, com.imi.rest.dao.model.Country country,
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, PurchaseRequest purchaseRequest, String teamuuid)
                    throws ClientProtocolException, IOException {
        String plivoNumber = (number.trim().replace("+", ""));
        forexValue = forexService.getForexValueByName("USD_GBP").getValue();
        return purchaseAop.purchasePlivoNumber(plivoNumber, numberType,
                provider, country, serviceTypeEnum, userid, clientId, groupid,
                teamid, clientname, clientkey, purchaseRequest, teamuuid);
    }
}
