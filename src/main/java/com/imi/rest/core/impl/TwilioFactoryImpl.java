package com.imi.rest.core.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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
import com.imi.rest.dao.CountryDao;
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.InboundApiErrorCodes;
import com.imi.rest.exception.InboundRestException;
import com.imi.rest.model.Address;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.ApplicationResponseList;
import com.imi.rest.model.Customer;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.IpAccessControlList;
import com.imi.rest.model.IpAccessControlListResponse;
import com.imi.rest.model.IpAddress;
import com.imi.rest.model.IpAddressResponse;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.OriginatingUrlResponse;
import com.imi.rest.model.OriginationUrl;
import com.imi.rest.model.PurchaseRequest;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.model.SipTrunk;
import com.imi.rest.model.SipTrunkResponse;
import com.imi.rest.model.SubAccountDetails;
import com.imi.rest.model.TwilioAccountResponse;
import com.imi.rest.model.TwilioAddressResponse;
import com.imi.rest.model.TwilioNumberPrice;
import com.imi.rest.service.ForexService;
import com.imi.rest.service.SubAccountService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class TwilioFactoryImpl implements NumberSearch, CountrySearch,
        PurchaseNumber, UrlConstants, ProviderConstants, NumberTypeConstants {

    boolean test = false;
    private static final Logger LOG = Logger.getLogger(TwilioFactoryImpl.class);

    private Map<String, TwilioNumberPrice> twilioMonthlyPriceMap;
    private Double forexValue;
    private Map<String, SubAccountDetails> twilioAccountMap;

    @Autowired
    ForexService forexService;

    @Autowired
    SubAccountService subAccountService;

    @Autowired
    CountryDao countryDao;

    @Autowired
    PurchaseAop purchaseAop;

    @Autowired
    ReleaseAop releaseAop;

    @Autowired
    UpdateNumberAop updateNumberAop;

    @Autowired
    NumberSearchAop numberSearchAop;

    @Autowired
    PricingAop pricingAop;

    @Autowired
    ImportCountryAop importCountryAop;

    @Override
    public void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String index,
            NumberResponse numberResponse, String markup)
            throws ClientProtocolException, IOException {
        if (!index.equalsIgnoreCase("")) {
            return;
        }
        forexValue = forexService.getForexValueByName("USD_GBP").getValue();
        if (twilioMonthlyPriceMap == null) {
            twilioMonthlyPriceMap = getTwilioPricing(countryIsoCode, provider);
        }
        numberSearchAop.searchTwilioNumbers(forexValue, twilioMonthlyPriceMap,
                provider, serviceTypeEnum, countryIsoCode, numberType, pattern,
                index, numberResponse, markup);
    }

    private Map<String, TwilioNumberPrice> getTwilioPricing(
            String countryIsoCode, Provider provider)
            throws ClientProtocolException, IOException {
        return pricingAop.getTwilioPricing(countryIsoCode, provider);
    }

    public Set<com.imi.rest.model.Country> importCountriesByUrl(
            Provider provider) throws JsonParseException, JsonMappingException,
            IOException {
        return importCountryAop.importTwilioCountriesByUrl(provider);
    }

    @Override
    public Set<com.imi.rest.model.Country> importCountries(
            Map<String, Map<String, String>> providerCapabilities)
            throws JsonParseException, JsonMappingException, IOException {
        return importCountryAop.importTwilioCountries(providerCapabilities);
    }

    @Override
    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, Country country,
            ServiceConstants serviceTypeEnum, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname,
            String clientkey, PurchaseRequest purchaseRequest, String teamuuid)
            throws ClientProtocolException, IOException {
        // get the subaccount details if not create a new one. You require a
        // unique identifier for each user. ideally storing the sid against the
        // user in imi database is better
        SubAccountDetails subAccountDetails = createSubAccount("" + userid,
                provider);
        return purchaseAop.purchaseTwilioNumber(number, numberType, provider,
                country, serviceTypeEnum, userid, clientId, groupid, teamid,
                clientname, clientkey, subAccountDetails, purchaseRequest,
                teamuuid);
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode, Integer userid, Integer clientId,
            Integer groupid, Integer teamid, String clientname, String clientkey)
            throws IOException {
        // get the subaccount details
        SubAccountDetails subAccountDetails = getSubAccountDetails("" + userid,
                provider);
        if (subAccountDetails == null) {
            String message = "No associated sub account created with the twilio for the client id "
                    + userid;
            LOG.error(message);
            throw InboundRestException
                    .createApiException(
                            InboundApiErrorCodes.TWILIO_SUBACCOUNT_USER_ASSOCIATION_EXCEPTION,
                            message);
        }
        String twilioNumber = "+" + (number.trim().replace("+", ""));
        ApplicationResponse incomingPhoneNumber = getIncomingPhoneNumber(
                twilioNumber, provider, subAccountDetails);
        releaseAop.releaseTwilioNumber(twilioNumber, provider, countryIsoCode,
                incomingPhoneNumber, subAccountDetails, clientkey);
    }

    public SubAccountDetails createSubAcctDetailsWithTrunk(Provider provider,
            int userid, int clientId, String masterTrunkSid)
            throws JsonParseException, JsonMappingException, IOException {
        SubAccountDetails subAccountDetails = createSubAccount("" + userid,
                provider);
        SipTrunk masterSipTrunk = getSipTrunkDetailsByTrunkSid(
                provider.getAuthId(), provider.getApiKey(), masterTrunkSid);
        List<OriginationUrl> masterOriginatingUrls = getOriginatingUrlDetails(
                masterSipTrunk.getSid(), provider.getAuthId(),
                provider.getApiKey());
        List<IpAccessControlList> masterAccessControlLists = getIpAccessControlList(
                masterSipTrunk.getSid(), provider.getAuthId(),
                provider.getApiKey());
        String subAccountSid = subAccountDetails.getSid();// "ACed9f2cfb6e04a3149787db7c11556cd2";
        String subAccountAuthToken = subAccountDetails.getAuth_token();// "113e3c3a958d933d4878f2b645df25db";
        SipTrunk subAccountSipTrunk = null;
        subAccountSipTrunk = createSipTrunkDetailsForSubAccount(
                masterSipTrunk.getFriendly_name(), subAccountSid,
                subAccountAuthToken, masterSipTrunk, userid);
        // String subAccountTrunkSid="TK79394565acfcdc0e32ad5770447bf4f0";
        // subAccountSipTrunk=getSipTrunkDetailsByTrunkSid(subAccountSid,subAccountAuthToken,
        // subAccountTrunkSid);
        for (OriginationUrl originatingUrl : masterOriginatingUrls) {
            createOriginatingUrlDetails(subAccountSipTrunk.getSid(),
                    subAccountSid, subAccountAuthToken, originatingUrl);
        }
        for (IpAccessControlList ipAccessControlList : masterAccessControlLists) {
            List<IpAddress> masterIpAddressList = getIpAddress(
                    provider.getAuthId(), provider.getApiKey(),
                    ipAccessControlList.getSid());
            IpAccessControlList subAccountIpAccessList = createIpAccessControlList(
                    ipAccessControlList.getFriendly_name(), subAccountSid,
                    subAccountAuthToken);
            for (IpAddress ipAddress : masterIpAddressList) {
                createIpAddress(ipAddress.getFriendly_name(),
                        ipAddress.getIp_address(), subAccountSid,
                        subAccountAuthToken, subAccountIpAccessList.getSid());
            }
            associateSipTrunkWithIpAccessControlList(
                    subAccountSipTrunk.getSid(),
                    subAccountIpAccessList.getSid(), subAccountSid,
                    subAccountAuthToken);
        }
        return subAccountDetails;
    }

    // TODO Customer SId must be obtained from the address response and
    // integrated with this method to replace provider AuthID
    public ApplicationResponse updateNumber(String number,
            ApplicationResponse applicationResponsetoModify, Provider provider,
            Integer userid, Integer clientId, Integer groupid, Integer teamid,
            String clientname, String clientkey)
            throws ClientProtocolException, IOException {
        // get the subaccount details
        if (test)
            return new ApplicationResponse();
        SubAccountDetails subAccountDetails = getSubAccountDetails("" + userid,
                provider);
        String twilioNumber = "+" + (number.trim().replace("+", ""));
        if (subAccountDetails == null) {
            String message = "No associated sub account created with the twilio for the client id "
                    + userid;
            LOG.error(message);
            throw InboundRestException
                    .createApiException(
                            InboundApiErrorCodes.TWILIO_SUBACCOUNT_USER_ASSOCIATION_EXCEPTION,
                            message);
        }
        List<SipTrunk> sipTrunkList = getSipTrunkDetailsBySubAccountSid(
                subAccountDetails.getSid(), subAccountDetails.getAuth_token());
        SipTrunk subAcctSipTrunk = null;
        if (sipTrunkList != null && sipTrunkList.size() > 0) {
            subAcctSipTrunk = sipTrunkList.get(0);
        }
        ApplicationResponse incomingPhoneNumber = getIncomingPhoneNumber(
                twilioNumber, provider, subAccountDetails);
        // checking number capabilities and setting only relevant url
        if (incomingPhoneNumber.getCapabilities().get("voice") != null
                && !incomingPhoneNumber.getCapabilities().get("voice")) {
            applicationResponsetoModify.setVoiceUrl(null);
            applicationResponsetoModify.setTrunkSid(null);
        } else {
            if (subAcctSipTrunk != null) {
                applicationResponsetoModify.setTrunkSid(subAcctSipTrunk
                        .getSid());
            } else {
                applicationResponsetoModify.setTrunkSid(null);
            }
        }
        if (incomingPhoneNumber.getCapabilities().get("sms") != null
                && !incomingPhoneNumber.getCapabilities().get("sms")) {
            applicationResponsetoModify.setSmsUrl(null);
        }
        return updateNumberAop.updateTwilioNumber(number.trim()
                .replace("+", ""), applicationResponsetoModify, provider,
                incomingPhoneNumber, userid, clientId, groupid, teamid,
                clientname, clientkey, subAccountDetails);
    }

    // TODO Customer SId must be obtained from the address response and
    // integrated with this method to replace provider AuthID
    private ApplicationResponse getIncomingPhoneNumber(String number,
            Provider provider, SubAccountDetails subAccountDetails)
            throws ClientProtocolException, IOException {
        number = number.replace("+", "");
        number = "+".concat(number);
        String twilioNumberGetUrl = TWILIO_PURCHASED_NUMBER_URL;
        twilioNumberGetUrl = twilioNumberGetUrl.replace("{auth_id}",
                subAccountDetails.getSid());
        ApplicationResponse applicationResponse = null;
        boolean recursive = true;
        int maxResults = 50;
        while (recursive) {
            String url = twilioNumberGetUrl.replace("{PageNo}", ""
                    + (maxResults - 50));
            GenericRestResponse restResponse = ImiHttpUtil
                    .defaultHttpGetHandler(url,
                            ImiBasicAuthUtil.getBasicAuthHash(provider));
            if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
                ApplicationResponseList incomingPhoneNumbers = ImiJsonUtil
                        .deserialize(restResponse.getResponseBody(),
                                ApplicationResponseList.class);
                if (incomingPhoneNumbers != null
                        && incomingPhoneNumbers.getObjects().size() > 0) {
                    loop: for (ApplicationResponse response : incomingPhoneNumbers
                            .getObjects()) {
                        if (response.getPhoneNumber().equalsIgnoreCase(number)) {
                            applicationResponse = response;
                            recursive = false;
                            break loop;
                        }
                    }
                    if (incomingPhoneNumbers.getEnd() > maxResults) {
                        maxResults = maxResults + 50;
                    } else {
                        recursive = false;
                    }
                }
            } else {
                recursive = false;
            }
        }
        return applicationResponse == null ? null : applicationResponse
                .getPhoneNumber().equals(number) ? applicationResponse : null;
    }

    public ApplicationResponse getIncomingPhoneNumberBySid(String numberSid,
            String subAccountSid, Provider provider)
            throws ClientProtocolException, IOException {
        String url = TWILIO_RELEASE_URL;
        url = url.replace("{auth_id}", subAccountSid).replace(
                "{IncomingPhoneNumberSid}", numberSid);
        ApplicationResponse incomingPhoneNumber = null;
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                url, ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            incomingPhoneNumber = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody(), ApplicationResponse.class);
        }
        return incomingPhoneNumber;
    }

    public SubAccountDetails getSubAccountDetailsBySid(String subAccountSid,
            Provider provider) throws ClientProtocolException, IOException {
        SubAccountDetails subAccountDetails = null;
        String url = TWILIO_SUB_ACCOUNT_URL;
        url = url.replace("{subaccountsid}", subAccountSid);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                url, ImiBasicAuthUtil.getBasicAuthHash(provider));
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            subAccountDetails = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody(), SubAccountDetails.class);
        } else {

        }
        return subAccountDetails;
    }

    public List<Address> getAddressOfCustomer(String customerName,
            Provider provider, Integer userid,
            SubAccountDetails customerAccountDetails)
            throws ClientProtocolException, IOException {
        String addressGetUrl = TWILIO_ADDRESS_URL;
        customerName = customerName.trim().replaceAll(" +", "+");
        String accountSid = provider.getAuthId();
        if (customerAccountDetails != null) {
            accountSid = customerAccountDetails.getSid();
        } else {
            customerAccountDetails = createSubAccount("" + userid, provider);
            accountSid = customerAccountDetails.getSid();
        }
        addressGetUrl = addressGetUrl.replace("{auth_id}", accountSid).concat(
                "?CustomerName=" + customerName);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                addressGetUrl, ImiBasicAuthUtil.getBasicAuthHash(provider));
        List<Address> addressList = null;
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            TwilioAddressResponse addressResponse = ImiJsonUtil
                    .deserialize(restResponse.getResponseBody(),
                            TwilioAddressResponse.class);
            addressList = addressResponse.getAddresses();
        } else {
            String message = "No associated sub account created with the twilio for the client id "
                    + userid;
            LOG.error(message);
            throw InboundRestException
                    .createApiException(
                            InboundApiErrorCodes.TWILIO_SUBACCOUNT_USER_ASSOCIATION_EXCEPTION,
                            message);
        }
        return addressList;
    }

    public Address createNewAddressOfCustomer(Customer customer,
            Provider provider, Integer userid) throws ClientProtocolException,
            IOException {
        SubAccountDetails customerAccountDetails = getSubAccountDetails(""
                + userid, provider);
        List<Address> addressList = getAddressOfCustomer(
                customer.getCustomer(), provider, userid,
                customerAccountDetails);
        String accountSid = provider.getAuthId();
        if (customerAccountDetails != null) {
            accountSid = customerAccountDetails.getSid();
        }
        String addressPostUrl = TWILIO_ADDRESS_URL;
        addressPostUrl = addressPostUrl.replace("{auth_id}", accountSid);
        Address address = null;
        Map<String, String> requestBody = new HashMap<String, String>();
        if (customer.getCountryIso() == null) {
            com.imi.rest.dao.model.Country country = countryDao
                    .getCountryByName(customer.getCountry());
            if (country != null) {
                customer.setCountryIso(country.getCountryIso());
            }
            loop: for (Address address2 : addressList) {
                if (address2.getIso_country().equalsIgnoreCase(
                        customer.getCountryIso())) {
                    address = address2;
                    break loop;
                }
            }
        }
        if (address == null) {
            requestBody.put("IsoCountry", customer.getCountryIso());
            requestBody.put("CustomerName", customer.getCustomer());
        } else {
            addressPostUrl = TWILIO_ADDRESS_BY_SID_URL.replace("{auth_id}",
                    accountSid).replace("{AddressSid}", address.getSid());
        }
        if (customer.getCity() != null) {
            requestBody.put("City", customer.getCity());
        }
        if (customer.getPostalcode() != null) {
            requestBody.put("PostalCode", customer.getPostalcode());
        }
        if (customer.getState() != null) {
            requestBody.put("Region", customer.getState());
        }
        if (customer.getStreet() != null) {
            requestBody.put("Street", customer.getStreet());
        }
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                addressPostUrl, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(provider), null);
        if (restResponse.getResponseCode() == HttpStatus.OK.value()
                || restResponse.getResponseCode() == HttpStatus.CREATED.value()) {
            address = ImiJsonUtil.deserialize(restResponse.getResponseBody(),
                    Address.class);
        } else {
            String message = "Exception while updating the customer address. Error response from Twilio "
                    + restResponse.getResponseBody();
            throw InboundRestException.createApiException(
                    InboundApiErrorCodes.TWILIO_ADRESS_CREATION_EXCEPTION,
                    message);
        }
        return address;
    }

    public SubAccountDetails createSubAccount(String friendlyName,
            Provider provider) throws JsonParseException, JsonMappingException,
            IOException {
        SubAccountDetails subAccountDetails = getSubAccountDetails(
                friendlyName, provider);
        if (subAccountDetails == null) {
            String twilioSubAccountCreateUrl = TWILIO_ACCOUNT_URL;
            twilioSubAccountCreateUrl = twilioSubAccountCreateUrl.replace(
                    "{auth_id}", provider.getAuthId()).replace(
                    "{friendlyName}", friendlyName);
            Map<String, String> requestBody = new HashMap<String, String>();
            requestBody.put("FriendlyName", friendlyName);
            GenericRestResponse restResponse = ImiHttpUtil
                    .defaultHttpPostHandler(twilioSubAccountCreateUrl,
                            requestBody,
                            ImiBasicAuthUtil.getBasicAuthHash(provider), null);
            if (restResponse.getResponseCode() == HttpStatus.OK.value()
                    || restResponse.getResponseCode() == HttpStatus.CREATED
                            .value()) {
                subAccountDetails = ImiJsonUtil
                        .deserialize(restResponse.getResponseBody(),
                                SubAccountDetails.class);
            } else {
                String message = "Exception while creating new SubAccount. Error response from Twilio "
                        + restResponse.getResponseBody();
                throw InboundRestException
                        .createApiException(
                                InboundApiErrorCodes.TWILIO_SUB_ACCOUNT_CREATION_EXCEPTION,
                                message);
            }
        }
        return subAccountDetails;
    }

    // need to change to accomodate fetching the subaccount details by sid
    // instead of hitting accounts to get the details
    public SubAccountDetails getSubAccountDetails(String friendlyName,
            Provider provider) throws JsonParseException, JsonMappingException,
            IOException {
        String twilioSubAccountCreateUrl = TWILIO_ACCOUNT_URL;
        twilioSubAccountCreateUrl = twilioSubAccountCreateUrl.replace(
                "{auth_id}", provider.getAuthId()).replace("{friendlyName}",
                friendlyName);
        if (twilioAccountMap == null) {
            twilioAccountMap = new HashMap<String, SubAccountDetails>();
        }
        if (twilioAccountMap.containsKey(friendlyName)) {
            twilioAccountMap.get(friendlyName);
        } else {
            GenericRestResponse restResponse = ImiHttpUtil
                    .defaultHttpGetHandler(twilioSubAccountCreateUrl,
                            ImiBasicAuthUtil.getBasicAuthHash(provider));
            if (restResponse.getResponseCode() == HttpStatus.OK.value()
                    || restResponse.getResponseCode() == HttpStatus.CREATED
                            .value()) {
                TwilioAccountResponse twilioAccountResponse = ImiJsonUtil
                        .deserialize(restResponse.getResponseBody(),
                                TwilioAccountResponse.class);
                for (SubAccountDetails twilioAccount : twilioAccountResponse
                        .getAccounts()) {
                    twilioAccountMap.put(twilioAccount.getFriendly_name(),
                            twilioAccount);
                }
            } else {
                String message = "Exception while creating new SubAccount. Error response from Twilio "
                        + restResponse.getResponseBody();
                throw InboundRestException
                        .createApiException(
                                InboundApiErrorCodes.TWILIO_SUB_ACCOUNT_CREATION_EXCEPTION,
                                message);
            }
        }
        return twilioAccountMap.get(friendlyName) == null ? null
                : twilioAccountMap.get(friendlyName);
    }

    public List<SipTrunk> getSipTrunkDetailsBySubAccountSid(String authId,
            String apiKey) throws ClientProtocolException, IOException {
        String url = TWILIO_LIST_ALL_SIP_TRUNK_URL;
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                url, ImiBasicAuthUtil.getBasicAuthHash(authId, apiKey));
        SipTrunkResponse sipTrunkResponse = ImiJsonUtil.deserialize(
                restResponse.getResponseBody(), SipTrunkResponse.class);
        return sipTrunkResponse.getTrunks();
    }

    public SipTrunk getSipTrunkDetailsByTrunkSid(String authId, String apiKey,
            String trunkSid) throws ClientProtocolException, IOException {
        String url = TWILIO_SIP_TRUNK_URL;
        url = url.replace("{trunk_sid}", trunkSid);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                url, ImiBasicAuthUtil.getBasicAuthHash(authId, apiKey));
        SipTrunk sipTrunk = ImiJsonUtil.deserialize(
                restResponse.getResponseBody(), SipTrunk.class);
        return sipTrunk;
    }

    public SipTrunk createSipTrunkDetailsForSubAccount(String trunkName,
            String subAccountSid, String subAccountAuthToken,
            SipTrunk masterSipTrunk, int clientId) throws ParseException,
            IOException {
        String url = TWILIO_LIST_ALL_SIP_TRUNK_URL;
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("FriendlyName", trunkName);
        if (masterSipTrunk.getDomain_name() != null) {
            requestBody.put("DomainName", masterSipTrunk.getDomain_name()
                    .replace(".pstn.twilio.com", "").concat("" + clientId)
                    + ".pstn.twilio.com");
        }
        if (masterSipTrunk.getDisaster_recovery_url() != null) {
            requestBody.put("DisasterRecoveryUrl",
                    masterSipTrunk.getDisaster_recovery_url());
        }
        if (masterSipTrunk.getDisaster_recovery_method() != null) {
            requestBody.put("DisasterRecoveryMethod",
                    masterSipTrunk.getDisaster_recovery_method());
        }
        if (masterSipTrunk.getSecure() != null) {
            requestBody.put("Secure", masterSipTrunk.getSecure());
        }
        SipTrunk subAccountSipTrunk = null;
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                url, requestBody, ImiBasicAuthUtil.getBasicAuthHash(
                        subAccountSid, subAccountAuthToken), null);
        if (restResponse.getResponseCode() == org.springframework.http.HttpStatus.CREATED
                .value()) {
            subAccountSipTrunk = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody(), SipTrunk.class);
        }
        return subAccountSipTrunk;
    }

    public List<OriginationUrl> getOriginatingUrlDetails(String trunkSid,
            String authId, String authToken) throws ClientProtocolException,
            IOException {
        String url = TWILIO_SIP_TRUNK_ORIGINATING_URL;
        url = url.replace("{trunk_sid}", trunkSid);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                url, ImiBasicAuthUtil.getBasicAuthHash(authId, authToken));
        OriginatingUrlResponse originatingUrlResponse = ImiJsonUtil
                .deserialize(restResponse.getResponseBody(),
                        OriginatingUrlResponse.class);
        return originatingUrlResponse.getOrigination_urls();
    }

    public void createOriginatingUrlDetails(String trunkSid, String authId,
            String authToken, OriginationUrl originatingUrl)
            throws ParseException, IOException {
        String url = TWILIO_SIP_TRUNK_ORIGINATING_URL;
        url = url.replace("{trunk_sid}", trunkSid);
        Map<String, String> requestBody = new HashMap<String, String>();
        // need to check
        if (originatingUrl.getFriendly_name() != null) {
            requestBody.put("FriendlyName", originatingUrl.getFriendly_name());
        }
        if (originatingUrl.getSip_url() != null) {
            requestBody.put("SipUrl", originatingUrl.getSip_url());
        }
        if (originatingUrl.getPriority() != null) {
            requestBody.put("Priority", originatingUrl.getPriority());
        }
        if (originatingUrl.getWeight() != null) {
            requestBody.put("Weight", originatingUrl.getWeight());
        }
        if (originatingUrl.getEnabled() != null) {
            requestBody.put("Enabled", originatingUrl.getEnabled());
        }
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                url, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(authId, authToken), null);
        OriginationUrl originationUrl = ImiJsonUtil.deserialize(
                restResponse.getResponseBody(), OriginationUrl.class);
    }

    public List<IpAccessControlList> getIpAccessControlList(String trunkSid,
            String authId, String authToken) throws ClientProtocolException,
            IOException {
        String url = TWILIO_SIP_TRUNK_IP_ACCESS_CONTROL_LIST_ASSOC_URL;
        url = url.replace("{trunk_sid}", trunkSid);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                url, ImiBasicAuthUtil.getBasicAuthHash(authId, authToken));
        IpAccessControlListResponse ipAccessControlListResponse = ImiJsonUtil
                .deserialize(restResponse.getResponseBody(),
                        IpAccessControlListResponse.class);
        System.out.println(restResponse.getResponseBody());
        return ipAccessControlListResponse.getIp_access_control_lists();
    }

    public IpAccessControlList createIpAccessControlList(String friendlyName,
            String authId, String authToken) throws ClientProtocolException,
            IOException {
        String url = TWILIO_IP_ACCESS_CONTROL_LIST_URL;
        url = url.replace("{auth_id}", authId);
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("FriendlyName", friendlyName);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                url, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(authId, authToken), null);
        IpAccessControlList ipAccessControlList = ImiJsonUtil.deserialize(
                restResponse.getResponseBody(), IpAccessControlList.class);
        return ipAccessControlList;
    }

    public void associateSipTrunkWithIpAccessControlList(String trunkSid,
            String ipAccessControlSid, String authId, String authToken)
            throws ClientProtocolException, IOException {
        String url = TWILIO_SIP_TRUNK_IP_ACCESS_CONTROL_LIST_ASSOC_URL;
        url = url.replace("{trunk_sid}", trunkSid);
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("IpAccessControlListSid", ipAccessControlSid);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                url, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(authId, authToken), null);
    }

    public List<IpAddress> getIpAddress(String authId, String authToken,
            String ipAccessControlSid) throws ClientProtocolException,
            IOException {
        String url = TWILIO_IP_ADDRESS_URL;
        url = url.replace("{access_control_list_sid}", ipAccessControlSid)
                .replace("{auth_id}", authId);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpGetHandler(
                url, ImiBasicAuthUtil.getBasicAuthHash(authId, authToken));
        IpAddressResponse ipAddressResponse = ImiJsonUtil.deserialize(
                restResponse.getResponseBody(), IpAddressResponse.class);
        return ipAddressResponse.getIp_addresses();
    }

    public IpAddress createIpAddress(String friendlyName, String ipAddress,
            String authId, String authToken, String ipAccessControlSid)
            throws ClientProtocolException, IOException {
        String url = TWILIO_IP_ADDRESS_URL;
        url = url.replace("{access_control_list_sid}", ipAccessControlSid)
                .replace("{auth_id}", authId);
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("FriendlyName", friendlyName);
        requestBody.put("IpAddress", ipAddress);
        GenericRestResponse restResponse = ImiHttpUtil.defaultHttpPostHandler(
                url, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(authId, authToken), null);
        return null;

    }

}
