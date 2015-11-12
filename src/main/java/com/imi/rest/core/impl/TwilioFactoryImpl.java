package com.imi.rest.core.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.exception.InvalidNumberException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.ApplicationResponseList;
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.InboundCallPrice;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.model.TwilioAccount;
import com.imi.rest.model.TwilioNumberPrice;
import com.imi.rest.service.CountrySearchService;
import com.imi.rest.service.ForexService;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class TwilioFactoryImpl implements NumberSearch, CountrySearch,
        PurchaseNumber, UrlConstants, ProviderConstants, NumberTypeConstants {

    private static final String TWILIO_CSV_FILE_PATH = "/Twilio - Number Prices.csv";
    private static final Logger LOG = Logger
            .getLogger(CountrySearchService.class);

    private TwilioNumberPrice numberTypePricing;
    private String priceUnit;
    private Map<String, TwilioNumberPrice> twilioMonthlyPriceMap;
    private Double forexValue;

    @Autowired
    ForexService forexService;

    @Override
    public void searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern, String index,
            NumberResponse numberResponse)
                    throws ClientProtocolException, IOException {
        if (!index.equalsIgnoreCase("")) {
            return;
        }
        if (twilioMonthlyPriceMap == null) {
            getTwilioPricing(countryIsoCode, provider);
        }
        List<Number> numberSearchList = numberResponse.getObjects() == null
                ? new ArrayList<Number>() : numberResponse.getObjects();
        String twilioPhoneSearchUrl = TWILIO_PHONE_SEARCH_URL;
        numberTypePricing = null;
        priceUnit = null;
        String servicesString = generateTwilioCapabilities(serviceTypeEnum);
        String type = "Local";
        if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "Mobile";
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "Tollfree";
        }
        twilioPhoneSearchUrl = twilioPhoneSearchUrl
                .replace("{auth_id}", provider.getAuthId())
                .replace("{country_iso}", countryIsoCode)
                .replace("{services}", servicesString)
                .replace("{pattern}", "*" + pattern.trim() + "*")
                .replace("{type}", type);
        GenericRestResponse twilioNumberSearchResponse = null;
        if (pattern.equalsIgnoreCase("")) {
            twilioPhoneSearchUrl = twilioPhoneSearchUrl.replace("Contains=**&",
                    "");
        }
        twilioNumberSearchResponse = ImiHttpUtil.defaultHttpGetHandler(
                twilioPhoneSearchUrl, ImiBasicAuthUtil.getBasicAuthHash(
                        provider.getAuthId(), provider.getApiKey()));
        if (twilioNumberSearchResponse.getResponseCode() == HttpStatus.OK
                .value()) {
            NumberResponse numberResponseFromTwilo = ImiJsonUtil.deserialize(
                    twilioNumberSearchResponse.getResponseBody(),
                    NumberResponse.class);
            List<Number> twilioNumberList = numberResponseFromTwilo == null
                    ? new ArrayList<Number>()
                    : numberResponseFromTwilo.getObjects() == null
                            ? new ArrayList<Number>()
                            : numberResponseFromTwilo.getObjects();
            String searchKey = countryIsoCode + "-" + type;
            if (numberTypePricing == null) {
                setNumberTypePricingMap(
                        twilioMonthlyPriceMap.get(searchKey.toUpperCase()));
            }
            // String voiceRate = numberTypePricing.get(type.toLowerCase());
            String voiceRate = numberTypePricing.getInboundVoicePrice();
            if (forexValue == null) {
                forexValue = forexService.getForexValueByName("USD_GBP")
                        .getValue();
            }
            for (Number twilioNumber : twilioNumberList) {
                if (twilioNumber != null) {
                    setServiceType(twilioNumber);
                    twilioNumber.setProvider(TWILIO);
                    twilioNumber.setType(
                            type.equalsIgnoreCase("local") ? "landline" : type);
                    twilioNumber.setPriceUnit(priceUnit);
                    String voiceRateInGBP = ImiDataFormatUtils
                            .forexConvert(forexValue, voiceRate);
                    twilioNumber.setVoiceRate(voiceRateInGBP);
                    String monthlyRentalRateInGBP = ImiDataFormatUtils
                            .forexConvert(forexValue,
                                    numberTypePricing.getMonthlyRentalRate());
                    twilioNumber.setMonthlyRentalRate(monthlyRentalRateInGBP);
                    numberSearchList.add(twilioNumber);
                }
            }
            numberResponse.setObjects(numberSearchList);
        }
    }

    private Map<String, TwilioNumberPrice> getTwilioPricing(
            String countryIsoCode, Provider provider)
                    throws ClientProtocolException, IOException {
        Map<String, String> numberTypePricingMap = new HashMap<String, String>();
        String pricingUrl = TWILIO_PRICING_URL;
        pricingUrl = pricingUrl.replace("{Country}", countryIsoCode);
        GenericRestResponse twilioPriceResponse;
        twilioPriceResponse = ImiHttpUtil.defaultHttpGetHandler(pricingUrl,
                ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()));
        CountryPricing countryPricing = null;
        if (twilioPriceResponse.getResponseCode() == HttpStatus.OK.value()) {
            countryPricing = ImiJsonUtil.deserialize(
                    twilioPriceResponse.getResponseBody(),
                    CountryPricing.class);
        }
        priceUnit = countryPricing.getPrice_unit();
        for (InboundCallPrice inboundCallPrice : countryPricing
                .getInbound_call_prices()) {
            String basePrice = inboundCallPrice.getBase_price() == null
                    ? inboundCallPrice.getCurrent_price()
                    : inboundCallPrice.getBase_price();
            numberTypePricingMap.put(
                    inboundCallPrice.getNumber_type().replace(" ", "").trim(),
                    basePrice);
        }
        if (twilioMonthlyPriceMap == null) {
            String line = "";
            String splitBy = ",";
            BufferedReader reader = null;
            twilioMonthlyPriceMap = new HashMap<String, TwilioNumberPrice>();
            try {
                InputStream in = getClass()
                        .getResourceAsStream(TWILIO_CSV_FILE_PATH);
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                int counter = 0;
                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(splitBy);
                    if (counter != 0 && row.length > 1) {
                        String isoCode = row[0];
                        String numberType = row[3];
                        try {
                            String twilioPricingMapKey = isoCode + "-"
                                    + (numberType.replace(" ", ""));
                            TwilioNumberPrice twilioNumberPrice = new TwilioNumberPrice();
                            twilioNumberPrice.setAddressRequired(row[16]);
                            twilioNumberPrice.setBetaStatus(row[15]);
                            twilioNumberPrice.setDomesticSmsOnly(row[9]);
                            twilioNumberPrice.setDomesticVoiceOnly(row[8]);
                            twilioNumberPrice.setInboundMmsPrice(row[14]);
                            twilioNumberPrice.setInboundSmsPrice(row[13]);
                            twilioNumberPrice.setInboundTrunkingPrice(row[12]);
                            twilioNumberPrice.setInboundVoicePrice(row[11]);
                            twilioNumberPrice.setMmsEnabled(row[7]);
                            twilioNumberPrice.setMonthlyRentalRate(row[10]);
                            twilioNumberPrice.setSmsEnabled(row[6]);
                            twilioNumberPrice.setTrunkingEnabled(row[5]);
                            twilioNumberPrice.setVoiceEnabled(row[4]);
                            twilioMonthlyPriceMap.put(
                                    twilioPricingMapKey.toUpperCase(),
                                    twilioNumberPrice);
                        } catch (Exception e) {
                        }
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
        }
        return twilioMonthlyPriceMap;
    }

    public List<String> numberType(String countryIsoCode) {
        String line = "";
        String splitBy = ",";
        BufferedReader reader = null;
        List<String> numberTypePerCountry = null;
        try {
            InputStream in = getClass()
                    .getResourceAsStream(TWILIO_CSV_FILE_PATH);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                numberTypePerCountry = new ArrayList<String>();
                String[] row = line.split(splitBy);
                if (counter != 0 && row.length > 1) {
                    String isoCode = row[0];
                    String numberType = row[3];
                    if (isoCode.equals(countryIsoCode)) {
                        String entry = isoCode + "-" + numberType;
                        numberTypePerCountry.add(entry);
                    }
                }
                counter++;
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return numberTypePerCountry;
    }

    @Override
    public void setServiceType(Number number) {
        Map<String, Boolean> capabilties = number.getCapabilities();
        if (capabilties.get("voice") && capabilties.get("SMS")) {
            number.setServiceType(ServiceConstants.BOTH.name());
            number.setSmsEnabled(true);
            number.setVoiceEnabled(true);
        } else if (capabilties.get("SMS")) {
            number.setServiceType(ServiceConstants.SMS.name());
            number.setSmsEnabled(true);
        } else {
            number.setServiceType(ServiceConstants.VOICE.name());
            number.setVoiceEnabled(true);
        }
    }

    private String generateTwilioCapabilities(
            ServiceConstants serviceTypeEnum) {
        String servicesString = null;
        switch (serviceTypeEnum) {
        case SMS:
            servicesString = "SmsEnabled=true";
            break;
        case VOICE:
            servicesString = "VoiceEnabled=true";
            break;
        case BOTH:
            servicesString = "SmsEnabled=true&VoiceEnabled=true";
            break;
        default:
            break;
        }
        return servicesString;
    }

    public Set<com.imi.rest.model.Country> importCountriesByUrl(
            Provider provider) throws JsonParseException, JsonMappingException,
                    IOException, ImiException {
        String url = TWILIO_COUNTRY_LIST_URL;
        String authHash = ImiBasicAuthUtil
                .getBasicAuthHash(provider.getAuthId(), provider.getApiKey());
        GenericRestResponse restResponse;
        Set<com.imi.rest.model.Country> countrySet = new HashSet<com.imi.rest.model.Country>();
        restResponse = ImiHttpUtil.defaultHttpGetHandler(url, authHash);
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            CountryResponse countryResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? ""
                            : restResponse.getResponseBody(),
                    CountryResponse.class);
            while (countryResponse != null
                    && countryResponse.getMeta().getNextPageUrl() != null) {
                String nextPageUrl = countryResponse.getMeta().getNextPageUrl();
                GenericRestResponse nextRestResponse = null;
                nextRestResponse = ImiHttpUtil
                        .defaultHttpGetHandler(nextPageUrl, authHash);
                if (nextRestResponse.getResponseCode() == HttpStatus.OK
                        .value()) {
                    CountryResponse countryResponse2 = ImiJsonUtil
                            .deserialize(
                                    nextRestResponse.getResponseBody() == null
                                            ? ""
                                            : nextRestResponse
                                                    .getResponseBody(),
                                    CountryResponse.class);
                    countryResponse
                            .addCountries(countryResponse2.getCountries());
                }

            }
            countrySet = countryResponse.getCountries();
        }
        return countrySet;
    }

    @Override
    public Set<com.imi.rest.model.Country> importCountries()
            throws JsonParseException, JsonMappingException, IOException {
        Set<com.imi.rest.model.Country> countrySet = new TreeSet<com.imi.rest.model.Country>();
        String line = "";
        String splitBy = ",";
        BufferedReader reader = null;
        try {
            InputStream in = getClass()
                    .getResourceAsStream(TWILIO_CSV_FILE_PATH);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(splitBy);
                if (counter != 0 && row.length > 1) {
                    com.imi.rest.model.Country country = new com.imi.rest.model.Country();
                    country.setCountry(row[1]);
                    country.setIsoCountry(row[0]);
                    country.setCountryCode(row[2]);
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

    @Override
    public PurchaseResponse purchaseNumber(String number, String numberType,
            Provider provider, Country country,
            ServiceConstants serviceTypeEnum)
                    throws ClientProtocolException, IOException, ImiException {
        String twilioPurchaseUrl = TWILIO_PURCHASE_URL;
        String twilioNumber = "+" + number.trim();
        twilioPurchaseUrl = twilioPurchaseUrl.replace("{number}", twilioNumber)
                .replace("{auth_id}", provider.getAuthId());
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("PhoneNumber", twilioNumber);
        GenericRestResponse response = ImiHttpUtil.defaultHttpPostHandler(
                twilioPurchaseUrl, requestBody,
                ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                null);
        String type = "Local";
        if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "Mobile";
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "Tollfree";
        }
        if (twilioMonthlyPriceMap == null) {
            getTwilioPricing(country.getCountryIso(), provider);
        }
        if (response.getResponseCode() == HttpStatus.CREATED.value()) {
            String searchKey = country.getCountryIso() + "-"
                    + type.toUpperCase();
            TwilioNumberPrice twilioPrice = twilioMonthlyPriceMap
                    .get(searchKey);
            PurchaseResponse purchaseResponse = new PurchaseResponse();
            purchaseResponse.setNumber(number);
            purchaseResponse.setNumberType(numberType);
            purchaseResponse.setRestrictions("");
            purchaseResponse.setMonthlyRentalRate(
                    ImiDataFormatUtils.forexConvert(forexValue,
                            twilioPrice.getMonthlyRentalRate()));
            purchaseResponse.setSetUpRate("");
            purchaseResponse.setSmsRate(ImiDataFormatUtils.forexConvert(
                    forexValue, twilioPrice.getInboundSmsPrice()));
            purchaseResponse.setVoicePrice(ImiDataFormatUtils.forexConvert(
                    forexValue, twilioPrice.getInboundVoicePrice()));
            purchaseResponse.setEffectiveDate("");
            purchaseResponse.setResourceManagerId(0);
            purchaseResponse.setCountryProviderId(country.getId());
            return purchaseResponse;
        } else {
            throw new InvalidNumberException(number, provider.getName());
        }
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode) throws IOException, InvalidNumberException {
        GenericRestResponse twilioAccountRestResponse = ImiHttpUtil
                .defaultHttpGetHandler(TWILIO_ACCOUNT_URL,
                        ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                                provider.getApiKey()));
        String accountSid = null;
        if (twilioAccountRestResponse.getResponseCode() == HttpStatus.OK
                .value()) {
            TwilioAccount twilioAccountResponse = ImiJsonUtil
                    .deserialize(twilioAccountRestResponse.getResponseBody(),
                            TwilioAccount.class);
            if (twilioAccountResponse!= null) {
                accountSid = twilioAccountResponse.getOwner_account_sid();
            }
        }
        // TODO: need to get IncomingPhoneNumberSid
        String incomingPhoneNumberSid = number;
        String twilioReleaseurl = TWILIO_RELEASE_URL;
        twilioReleaseurl = twilioReleaseurl.replace("{AccountSid}", accountSid)
                .replace("{IncomingPhoneNumberSid}", incomingPhoneNumberSid);
        GenericRestResponse response = ImiHttpUtil.defaultHttpDeleteHandler(
                twilioReleaseurl, new HashMap<String, String>(),
                ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                null);
        if (response.getResponseCode() != HttpStatus.NO_CONTENT.value()) {
            throw new InvalidNumberException(number, provider.getName());
        }
    }

    public TwilioNumberPrice getNumberTypePricingMap() {
        return numberTypePricing;
    }

    public void setNumberTypePricingMap(TwilioNumberPrice numberTypePricing) {
        this.numberTypePricing = numberTypePricing;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public ApplicationResponse updateNumber(String number,
            ApplicationResponse applicationResponsetoModify, Provider provider)
                    throws ClientProtocolException, IOException, ImiException {
        String twilioNumberUpdateUrl = TWILIO_NUMBER_UPDATE_URL;
        ApplicationResponse incomingPhoneNumber = getIncomingPhoneNumber(number,
                provider);
        twilioNumberUpdateUrl = twilioNumberUpdateUrl.replace(
                "{IncomingPhoneNumberSid}", incomingPhoneNumber.getSid());
        twilioNumberUpdateUrl = getUpdatedUrl(twilioNumberUpdateUrl,
                applicationResponsetoModify);
        ApplicationResponse applicationResponse = new ApplicationResponse();
        GenericRestResponse response = ImiHttpUtil.defaultHttpPostHandler(
                twilioNumberUpdateUrl, new HashMap<String, String>(),
                ImiBasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                        provider.getApiKey()),
                null);
        // TODO check the rest response code and serialize only on success
        // condition
        applicationResponse = ImiJsonUtil.deserialize(
                response.getResponseBody(), ApplicationResponse.class);
        return applicationResponse;
    }

    /***
     * 
     * @param number
     * @param provider
     * @return
     * @throws ImiException
     * @throws IOException
     * @throws ClientProtocolException
     */
    private ApplicationResponse getIncomingPhoneNumber(String number,
            Provider provider)
                    throws ClientProtocolException, IOException, ImiException {
        String twilioNumberGetUrl = TWILIO_PURCHASE_URL;
        twilioNumberGetUrl = twilioNumberGetUrl.replace("{number}", number);
        ApplicationResponse applicationResponse = new ApplicationResponse();
        GenericRestResponse response = ImiHttpUtil.defaultHttpGetHandler(
                twilioNumberGetUrl, ImiBasicAuthUtil.getBasicAuthHash(
                        provider.getAuthId(), provider.getApiKey()));
        if (response.getResponseCode() == HttpStatus.OK.value()) {
            ApplicationResponseList incomingPhoneNumbers = ImiJsonUtil
                    .deserialize(response.getResponseBody(),
                            ApplicationResponseList.class);
            applicationResponse = incomingPhoneNumbers.getObjects().get(0);
        }
        return applicationResponse;
    }

    private String getUpdatedUrl(String url,
            ApplicationResponse modifyapplication) {
        String toAppend = "?";
        if (modifyapplication.getFriendlyName() != null) {
            toAppend.concat("FriendlyName="
                    + modifyapplication.getFriendlyName() + "&");
        }
        if (modifyapplication.getApiVersion() != null) {
            toAppend.concat(
                    "ApiVersion=" + modifyapplication.getApiVersion() + "&");
        }
        if (modifyapplication.getVoiceUrl() != null) {
            toAppend.concat(
                    "VoiceUrl=" + modifyapplication.getVoiceUrl() + "&");
        }
        if (modifyapplication.getVoiceMethod() != null) {
            toAppend.concat(
                    "VoiceMethod=" + modifyapplication.getVoiceMethod() + "&");
        }
        if (modifyapplication.getVoiceFallback() != null) {
            toAppend.concat("VoiceFallbackUrl="
                    + modifyapplication.getVoiceFallback() + "&");
        }
        if (modifyapplication.getVoiceFallbackMethod() != null) {
            toAppend.concat("VoiceFallbackMethod="
                    + modifyapplication.getVoiceFallbackMethod() + "&");
        }
        if (modifyapplication.getStatusCallback() != null) {
            toAppend.concat("StatusCallback="
                    + modifyapplication.getStatusCallback() + "&");
        }
        if (modifyapplication.getStatusCallbackMethod() != null) {
            toAppend.concat("StatusCallbackMethod="
                    + modifyapplication.getStatusCallbackMethod() + "&");
        }
        if (modifyapplication.getVoiceCallerIdLookup() != null) {
            toAppend.concat("VoiceCallerIdLookup="
                    + modifyapplication.getVoiceCallerIdLookup() + "&");
        }
        if (modifyapplication.getVoiceApplicationSid() != null) {
            toAppend.concat("VoiceApplicationSid="
                    + modifyapplication.getVoiceApplicationSid() + "&");
        }
        if (modifyapplication.getTrunkSid() != null) {
            toAppend.concat(
                    "TrunkSid=" + modifyapplication.getTrunkSid() + "&");
        }
        if (modifyapplication.getSmsUrl() != null) {
            toAppend.concat("SmsUrl=" + modifyapplication.getSmsUrl() + "&");
        }
        if (modifyapplication.getSmsFallbackUrl() != null) {
            toAppend.concat("SmsFallbackUrl="
                    + modifyapplication.getSmsFallbackUrl() + "&");
        }
        if (modifyapplication.getSmsFallbackMethod() != null) {
            toAppend.concat("SmsFallbackMethod="
                    + modifyapplication.getSmsFallbackMethod() + "&");
        }
        if (modifyapplication.getSmsApplicationSid() != null) {
            toAppend.concat("SmsApplicationSid="
                    + modifyapplication.getSmsApplicationSid() + "&");
        }
        if (modifyapplication.getAccountSid() != null) {
            toAppend.concat(
                    "AccountSid=" + modifyapplication.getAccountSid() + "&");
        }
        toAppend = toAppend.substring(0, toAppend.length() - 1);
        url.concat(toAppend);
        return url;
    }

}
