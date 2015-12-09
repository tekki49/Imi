package com.imi.rest.core.aop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.imi.rest.constants.NumberTypeConstants;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.dao.model.Purchase;
import com.imi.rest.dao.model.ResourceMaster;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.NexmoPurchaseResponse;
import com.imi.rest.model.PlivoPurchaseResponse;
import com.imi.rest.model.PurchaseRequest;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.model.SubAccountDetails;
import com.imi.rest.service.ResourceService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class PurchaseAop
        implements UrlConstants, ProviderConstants, NumberTypeConstants {

    boolean test = false;
    @Autowired
    ResourceService resourceService;

    private static final Logger LOG = Logger.getLogger(PurchaseAop.class);

    public PurchaseResponse purchaseTwilioNumber(String number,
            String numberType, Provider provider, Country country,
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, SubAccountDetails subAccountDetails,
            PurchaseRequest purchaseRequest, String teamuuid)
            throws ClientProtocolException, IOException {
        String twilioPurchaseUrl = TWILIO_PURCHASE_URL;
        String twilioNumber = "+" + (number.trim().replace("+", ""));
        twilioPurchaseUrl = twilioPurchaseUrl.replace("{number}", twilioNumber)
                .replace("{auth_id}", subAccountDetails.getSid());
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("PhoneNumber", twilioNumber);
        GenericRestResponse restResponse = new GenericRestResponse();
        if (numberType.equalsIgnoreCase(LANDLINE)
                || numberType.equalsIgnoreCase(LOCAL)) {
            numberType = LANDLINE;
        } else if (numberType.equalsIgnoreCase(MOBILE)) {
            numberType = MOBILE;
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            numberType = TOLLFREE;
        } else if (numberType.equalsIgnoreCase(MULTI)) {
            numberType = MULTI;
        }
        if (!test) {
            restResponse = ImiHttpUtil.defaultHttpPostHandler(twilioPurchaseUrl,
                    requestBody, ImiBasicAuthUtil.getBasicAuthHash(provider),
                    null);
        }
        if (test || restResponse.getResponseCode() == HttpStatus.CREATED
                .value()) {
            ApplicationResponse numberDetails = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? "{}"
                            : restResponse.getResponseBody(),
                    ApplicationResponse.class);
            PurchaseResponse purchaseResponse = new PurchaseResponse();
            if (("none").equalsIgnoreCase(
                    numberDetails.getAddress_requirements())) {
                purchaseResponse.setAddressRequired(false);
            } else {
                purchaseResponse.setAddressRequired(true);
            }
            purchaseResponse.setNumber(number);
            purchaseResponse.setNumberType(numberType);
            purchaseResponse.setMonthlyRentalRate(
                    purchaseRequest.getMonthlyRentalRate());
            purchaseResponse.setSetUpRate("");
            purchaseResponse.setSmsRate(purchaseRequest.getSmsRate());
            purchaseResponse.setVoicePrice(purchaseRequest.getVoiceRate());
            ResourceMaster resourceMaster = resourceService.updateResource(
                    purchaseRequest.getNumber(), purchaseResponse,
                    serviceTypeEnum, provider, country, userid, clientId,
                    groupid, teamid, clientname, clientkey, teamuuid,
                    numberType.toUpperCase());
            resourceService.updateResourceAllocation(resourceMaster, userid,
                    clientId, groupid, teamid, clientname, clientkey);
            resourceService.updateChannelAssetsAllocation(resourceMaster,
                    userid, clientId, groupid, teamid, clientname, clientkey);
            purchaseResponse
                    .setEffectiveDate(ImiDataFormatUtils.getCurrentTimeStamp());
            purchaseResponse.setCountryIso(country.getCountryIso());
            Purchase purchase = resourceService.updatePurchase(purchaseResponse,
                    numberType,
                    ImiDataFormatUtils.getAddressRestrictions(
                            purchaseResponse.isAddressRequired(),
                            numberDetails.getAddress_requirements()),
                    resourceMaster);
            resourceService.updatePurchasehistory(purchase);
            return purchaseResponse;
        } else {
            LOG.error("Exception occured while purchasing number " + number
                    + " from service provider " + provider.getName()
                    + " Response from service provider is : "
                    + restResponse.getResponseBody() + " with response code :"
                    + restResponse.getResponseCode());
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_NUMBER_PURCHASE_EXCEPTION,
                    "Error while purchasing number " + number + " from "
                    + provider.getName()
                    + " Response from service provider is "
                    + restResponse.getResponseBody());
        }
    }

    public PurchaseResponse purchaseNexmoNumber(String number,
            String numberType, Provider provider,
            com.imi.rest.dao.model.Country country,
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, PurchaseRequest purchaseRequest, String teamuuid)
            throws ClientProtocolException, IOException {
        String nexmoPurchaseUrl = NEXMO_PURCHASE_URL;
        String nexmoNumber = (number.trim().replace("+", ""));
        nexmoPurchaseUrl = nexmoPurchaseUrl
                .replace("{api_key}", provider.getAuthId())
                .replace("{api_secret}", provider.getApiKey())
                .replace("{country}", country.getCountryIso())
                .replace("{msisdn}", "" + nexmoNumber);
        GenericRestResponse restResponse = new GenericRestResponse();
        if (!test) {
            restResponse = ImiHttpUtil.defaultHttpPostHandler(nexmoPurchaseUrl,
                    new HashMap<String, String>(),
                    ImiBasicAuthUtil.getBasicAuthHash(provider),
                    ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        }
        String type = LANDLINE;
        if (numberType.equalsIgnoreCase(MOBILE)) {
            type = MOBILE;
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = TOLLFREE;
        } else if (numberType.equalsIgnoreCase(MULTI)) {
            type = MULTI;
        }
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        NexmoPurchaseResponse nexmoPurchaseResponse = ImiJsonUtil.deserialize(
                restResponse.getResponseBody() == null ? "{}"
                        : restResponse.getResponseBody(),
                NexmoPurchaseResponse.class);
        if (test || restResponse.getResponseCode() == HttpStatus.OK.value()) {
            if (test || ("200".equals(nexmoPurchaseResponse.getErrorcode())
                    && "success".equals(
                            nexmoPurchaseResponse.getErrorCodeLabel()))) {
                purchaseResponse.setNumber(nexmoNumber);
                purchaseResponse.setNumberType(type);
                purchaseResponse.setMonthlyRentalRate(
                        purchaseRequest.getMonthlyRentalRate());
                purchaseResponse.setVoicePrice(purchaseRequest.getVoiceRate());
                purchaseResponse.setSmsRate(purchaseRequest.getSmsRate());
                purchaseResponse.setAddressRequired(false);
                ResourceMaster resourceMaster = resourceService.updateResource(
                        purchaseRequest.getNumber(), purchaseResponse,
                        serviceTypeEnum, provider, country, userid, clientId,
                        groupid, teamid, clientname, clientkey, teamuuid, type);
                resourceService.updateResourceAllocation(resourceMaster, userid,
                        clientId, groupid, teamid, clientname, clientkey);
                resourceService.updateChannelAssetsAllocation(resourceMaster,
                        userid, clientId, groupid, teamid, clientname,
                        clientkey);
                purchaseResponse.setCountryIso(country.getCountryIso());
                Purchase purchase = resourceService.updatePurchase(
                        purchaseResponse, numberType,
                        ImiDataFormatUtils.getAddressRestrictions(
                                purchaseResponse.isAddressRequired(), null),
                        resourceMaster);
                resourceService.updatePurchasehistory(purchase);
                return purchaseResponse;
            }
        } else if (restResponse.getResponseCode() == HttpStatus.UNAUTHORIZED
                .value()) {
            LOG.error("Exception occured while purchasing number " + number
                    + " from service provider " + provider.getName()
                    + " Response from service provider is : "
                    + restResponse.getResponseBody() + " with response code :"
                    + restResponse.getResponseCode());
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_PROVIDER_EXCEPTION,
                    "Error while purchasing number " + nexmoNumber + " from "
                    + provider.getName()
                    + " Response from service provider is "
                    + restResponse.getResponseBody());
        } else if (restResponse.getResponseCode() == HttpStatus.METHOD_FAILURE
                .value()) {
            LOG.error("Exception occured while purchasing number " + number
                    + " from service provider " + provider.getName()
                    + " Response from service provider is : "
                    + restResponse.getResponseBody() + " with response code :"
                    + restResponse.getResponseCode());
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_NUMBER_PURCHASE_EXCEPTION,
                    "Error while purchasing number " + nexmoNumber + " from "
                    + provider.getName()
                    + " Response from service provider is "
                    + restResponse.getResponseBody());
        } else {
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_NUMBER_PURCHASE_EXCEPTION,
                    "Some Error occured while purchasing the number "
                    + nexmoNumber + " please try again"
                    + " Response from service provider is "
                    + restResponse.getResponseBody());
        }
        return purchaseResponse;
    }

    public PurchaseResponse purchasePlivoNumber(String number,
            String numberType, Provider provider,
            com.imi.rest.dao.model.Country country,
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, PurchaseRequest purchaseRequest, String teamuuid)
            throws ClientProtocolException, IOException {
        if (numberType.equalsIgnoreCase(LANDLINE)
                || numberType.equalsIgnoreCase(LOCAL)) {
            numberType = LANDLINE;
        } else if (numberType.equalsIgnoreCase(MOBILE)) {
            numberType = MOBILE;
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            numberType = TOLLFREE;
        } else if (numberType.equalsIgnoreCase(MULTI)) {
            numberType = MULTI;
        }
        String plivoNumber = (number.trim().replace("+", ""));
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        String plivoPurchaseUrl = PLIVO_PURCHASE_URL
                .replace("{auth_id}", provider.getAuthId())
                .replace("{number}", plivoNumber);
        GenericRestResponse restResponse = new GenericRestResponse();
        if (!test) {
            restResponse = ImiHttpUtil.defaultHttpPostHandler(plivoPurchaseUrl,
                    new HashMap<String, String>(),
                    ImiBasicAuthUtil.getBasicAuthHash(provider),
                    ContentType.APPLICATION_JSON.getMimeType());
        }
        PlivoPurchaseResponse plivoPurchaseResponse = ImiJsonUtil.deserialize(
                restResponse.getResponseBody() == null ? "{}"
                        : restResponse.getResponseBody(),
                PlivoPurchaseResponse.class);
        if (test || restResponse.getResponseCode() == HttpStatus.CREATED
                .value()) {
            if (test || plivoPurchaseResponse.getNumberStatus().get(0)
                    .getStatus().equalsIgnoreCase("pending")) {
                purchaseResponse.setAddressRequired(true);
            } else {
                purchaseResponse.setAddressRequired(false);
            }
            if (test || "Success"
                    .equalsIgnoreCase(plivoPurchaseResponse.getStatus())) {
                purchaseResponse.setStatus("Success");
            }
            purchaseResponse.setNumber(plivoNumber);
            purchaseResponse.setNumberType(numberType);
            purchaseResponse.setMonthlyRentalRate(
                    purchaseRequest.getMonthlyRentalRate());
            // purchaseResponse.setSetUpRate(purchaseRequest.getSetUpRate());
            purchaseResponse.setSmsRate(purchaseRequest.getSmsRate());
            purchaseResponse.setVoicePrice(purchaseRequest.getVoiceRate());
            purchaseResponse
                    .setEffectiveDate(ImiDataFormatUtils.getCurrentTimeStamp());
            ResourceMaster resourceMaster = resourceService.updateResource(
                    purchaseRequest.getNumber(), purchaseResponse,
                    serviceTypeEnum, provider, country, userid, clientId,
                    groupid, teamid, clientname, clientkey, teamuuid,
                    numberType.toUpperCase());
            purchaseResponse.setCountryIso(country.getCountryIso());
            resourceService.updateResourceAllocation(resourceMaster, userid,
                    clientId, groupid, teamid, clientname, clientkey);
            resourceService.updateChannelAssetsAllocation(resourceMaster,
                    userid, clientId, groupid, teamid, clientname, clientkey);
            // need to verify the type of restriction. putting any as default
            Purchase purchase = resourceService.updatePurchase(purchaseResponse,
                    numberType,
                    ImiDataFormatUtils.getAddressRestrictions(
                            purchaseResponse.isAddressRequired(),
                            purchaseResponse.isAddressRequired() ? "any"
                                    : null),
                    resourceMaster);
            resourceService.updatePurchasehistory(purchase);
        } else if (plivoPurchaseResponse.getError() != null) {
            LOG.error("Exception occured while purchasing number " + number
                    + " from service provider " + provider.getName()
                    + " Response from service provider is : "
                    + restResponse.getResponseBody() + " with response code :"
                    + restResponse.getResponseCode());
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_NUMBER_PURCHASE_EXCEPTION,
                    "error occured for number provided." + plivoNumber
                    + " Error :" + plivoPurchaseResponse.getError());
        } else {
            LOG.error("Exception occured while purchasing number " + number
                    + " from service provider " + provider.getName()
                    + " Response from service provider is : "
                    + restResponse.getResponseBody() + " with response code :"
                    + restResponse.getResponseCode());
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.INVALID_NUMBER_PURCHASE_EXCEPTION,
                    "error occured for number provided." + plivoNumber
                    + " Response from Service provider  :"
                    + restResponse.getResponseBody());
        }
        return purchaseResponse;
    }

}
