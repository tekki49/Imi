package com.imi.rest.core.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
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
import com.imi.rest.core.aop.ServiceTypeAop;
import com.imi.rest.core.aop.UpdateNumberAop;
import com.imi.rest.dao.model.Provider;

import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.BalanceResponse;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PurchaseRequest;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.ForexService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class NexmoFactoryImpl implements NumberSearch, CountrySearch,
        PurchaseNumber, UrlConstants, ProviderConstants, NumberTypeConstants {

    boolean test = false;
    private Double forexValue;
    private static Map<String, CountryPricing> countryPricingMap;

    @Autowired
    ForexService forexService;

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

    @Autowired
    ServiceTypeAop serviceTypeAop;

    @Override
    public void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String nexmoIndex,
            NumberResponse numberResponse, String markup)
            throws ClientProtocolException, IOException {
        List<Number> numberSearchList = new ArrayList<Number>();
        String type = LANDLINE;
        if (numberType.equalsIgnoreCase(MOBILE)) {
            type = MOBILE;
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = TOLLFREE;
        } else if (numberType.equalsIgnoreCase(MULTI)) {
            type = MULTI;
        } else if (numberType.equalsIgnoreCase(LANDLINE)
                || numberType.equalsIgnoreCase(LOCAL)) {
            type = LANDLINE;
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
        if (countryPricingMap == null) {
            countryPricingMap = pricingAop.getNexmoPricing(provider);
        }
        searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, type,
                pattern, numberSearchList, index, numberResponse, "FIRST",
                markup);
        List<Number> numberList = numberResponse.getObjects() == null ? new ArrayList<Number>()
                : numberResponse.getObjects();
        numberList.addAll(numberSearchList);
    }

    void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, List<Number> numberSearchList,
            int index, NumberResponse numberResponse,
            String previousNexmoIndex, String markup)
            throws ClientProtocolException, IOException {
        // if(numberSearchList.size()>THRESHOLD)return;
        forexValue = forexService.getForexValueByName("EUR_GBP").getValue();
        if (countryPricingMap == null) {
            countryPricingMap = pricingAop.getNexmoPricing(provider);
        }
        numberSearchAop.searchNexmoPhoneNumbers(provider, serviceTypeEnum,
                countryIsoCode, numberType, pattern, numberSearchList, index,
                numberResponse, previousNexmoIndex, forexValue,
                countryPricingMap, markup);
    }

    @Override
    public Set<Country> importCountries(
            Map<String, Map<String, String>> providerCapabilities)
            throws FileNotFoundException, JsonParseException,
            JsonMappingException, IOException {
        return importCountryAop.importNexmoCountries(providerCapabilities);
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname, String clientkey)
            throws ClientProtocolException, IOException {
        String nexmoNumber = (number.trim().replace("+", ""));
        Number numberDetails = getPurchaseNumberDetails(nexmoNumber, provider);
        releaseAop.releaseNexmoNumber(nexmoNumber, provider, countryIsoCode,
                numberDetails, clientkey);
    }

    public BalanceResponse checkBalance(Provider provider)
            throws ClientProtocolException, IOException {
        String nexoAccountBalanceurl = NEXMO_ACCOUNT_BALANCE_URL;
        nexoAccountBalanceurl = nexoAccountBalanceurl.replace("{api_key}",
                provider.getAuthId()).replace("{api_secret}",
                provider.getApiKey());
        BalanceResponse balanceResponse = new BalanceResponse();
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                nexoAccountBalanceurl,
                ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            balanceResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody(), BalanceResponse.class);
        }
        if (balanceResponse.getAccountBalance() != null) {
            balanceResponse.setAccountBalance(balanceResponse
                    .getAccountBalance() + " EUR");
        }
        return balanceResponse;
    }

    public Number getPurchaseNumberDetails(String number, Provider provider)
            throws ClientProtocolException, IOException {
        String nexmoPurchasedNumberUrl = NEXMO_PURCHASED_NUMBER_URL;
        nexmoPurchasedNumberUrl = nexmoPurchasedNumberUrl.replace("{api_key}",
                provider.getAuthId()).replace("{api_secret}",
                provider.getApiKey());
        Number numberDetails = null;
        GenericRestResponse restResponse = ImiHttpUtil
                .defaultHttpGetHandler(nexmoPurchasedNumberUrl);
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            NumberResponse nexmoNumberResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? "" : restResponse
                            .getResponseBody(), NumberResponse.class);
            loop: for (Number purchasedNumber : nexmoNumberResponse
                    .getObjects()) {
                if (purchasedNumber.getNumber().equalsIgnoreCase(number)) {
                    numberDetails = purchasedNumber;
                    break loop;
                }
            }
        }
        return numberDetails == null ? null : numberDetails.getNumber().equals(
                number) ? numberDetails : null;
    }

    public ApplicationResponse updateNumber(String number,
            String countryIsoCode, ApplicationResponse application,
            Provider provider, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname, String clientkey)
            throws ClientProtocolException, IOException {
        if (test)
            return new ApplicationResponse();
        String nexmoNumber = (number.trim().replace("+", ""));
        Number numberDetails = getPurchaseNumberDetails(nexmoNumber, provider);
        serviceTypeAop.setNexmoServiceType(numberDetails);
        // checking number capabilities and setting only relevant url
        if (!numberDetails.isSmsEnabled()) {
            application.setMoHttpUrl(null);
        }
        if (!numberDetails.isVoiceEnabled()) {
            application.setVoiceCallbackValue(null);
        }
        return updateNumberAop.updateNexmoNumber(nexmoNumber, countryIsoCode,
                application, provider, numberDetails, userid, clientId,
                groupid, teamid, clientname, clientkey);
    }

    @Override
    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, com.imi.rest.dao.model.Country country,
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, PurchaseRequest purchaseRequest, String teamuuid)
            throws ClientProtocolException, IOException {
        forexValue = forexService.getForexValueByName("EUR_GBP").getValue();
        if (countryPricingMap == null) {
            countryPricingMap = pricingAop.getNexmoPricing(provider);
        }
        return purchaseAop.purchaseNexmoNumber(number, numberType, provider,
                country, serviceTypeEnum, userid, clientId, groupid, teamid,
                clientname, clientkey, purchaseRequest, teamuuid);
    }

}
