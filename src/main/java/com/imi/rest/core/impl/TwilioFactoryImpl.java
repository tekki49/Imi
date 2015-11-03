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
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.imi.rest.dao.model.Country;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.InboundCallPrice;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.CountrySearchService;
import com.imi.rest.service.ProviderService;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.DataFormatUtils;
import com.imi.rest.util.HttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class TwilioFactoryImpl
        implements NumberSearch, CountrySearch, PurchaseNumber, UrlConstants,
        ProviderConstants, NumberTypeConstants, ForexConstants {

    private static final String TWILIO_CSV_FILE_PATH = "/Twilio - Number Prices.csv";
    private static final Logger LOG = Logger
            .getLogger(CountrySearchService.class);

    private Map<String, String> numberTypePricingMap;
    private String priceUnit;
    private Map<String, Map<String, String>> twilioMonthlyPriceMap;

    @Autowired
    private ProviderService providerService;

    @Override
    public List<Number> searchPhoneNumbers(Provider provider,
            ServiceConstants serviceTypeEnum, String countryIsoCode,
            String numberType, String pattern)
                    throws ClientProtocolException, IOException {
        getTwilioPricing(countryIsoCode, provider);
        List<Number> phoneSearchResult = new ArrayList<Number>();
        String twilioPhoneSearchUrl = TWILIO_PHONE_SEARCH_URL;
        numberTypePricingMap = null;
        priceUnit = null;
        String servicesString = generateTwilioCapabilities(serviceTypeEnum);
        String type = "Local";
        if (numberType.equalsIgnoreCase(MOBILE)) {
            type = "Mobile";
        } else if (numberType.equalsIgnoreCase(TOLLFREE)) {
            type = "Tollfree";
        }
        twilioPhoneSearchUrl = twilioPhoneSearchUrl
                .replace("{country_iso}", countryIsoCode)
                .replace("{services}", servicesString)
                .replace("{pattern}", pattern.trim()).replace("{type}", type);
        if (pattern.trim().equals("")) {
            twilioPhoneSearchUrl = twilioPhoneSearchUrl.replace("Contains=&",
                    "");
        }
        String response = "";
        try {
            response = HttpUtil.defaultHttpGetHandler(twilioPhoneSearchUrl,
                    BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                            provider.getApiKey()));
        } catch (ImiException e) {
            return phoneSearchResult;
        }
        NumberResponse numberResponse = ImiJsonUtil.deserialize(response,
                NumberResponse.class);
        List<Number> twilioNumberList = numberResponse == null
                ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        if (numberTypePricingMap == null) {
            setNumberTypePricingMap(getTwilioPricing(countryIsoCode, provider));
        }
        String voiceRate = numberTypePricingMap.get(type.toLowerCase());
        for (Number twilioNumber : twilioNumberList) {
            if (twilioNumber != null) {
                setServiceType(twilioNumber);
                twilioNumber.setProvider(TWILIO);
                twilioNumber.setType(
                        type.equalsIgnoreCase("local") ? "landline" : type);
                twilioNumber.setPriceUnit(priceUnit);

                String voiceRateInGBP = DataFormatUtils.forexConvert(USD_GBP,
                        voiceRate);
                twilioNumber.setVoiceRate(voiceRateInGBP);
                String monthlyRentalRateInGBP = DataFormatUtils.forexConvert(
                        USD_GBP,
                        twilioMonthlyPriceMap.get(countryIsoCode).get(type));
                twilioNumber.setMonthlyRentalRate(monthlyRentalRateInGBP);
                phoneSearchResult.add(twilioNumber);
            }
        }
        return phoneSearchResult;
    }

    private Map<String, String> getTwilioPricing(String countryIsoCode,
            Provider provider) throws ClientProtocolException, IOException {
        Map<String, String> numberTypePricingMap = new HashMap<String, String>();
        String pricingUrl = TWILIO_PRICING_URL;
        pricingUrl = pricingUrl.replace("{Country}", countryIsoCode);
        String twilioPriceResponse;
        try {
            twilioPriceResponse = HttpUtil.defaultHttpGetHandler(pricingUrl,
                    BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                            provider.getApiKey()));
        } catch (ImiException e) {
            return numberTypePricingMap;
        }
        CountryPricing countryPricing = ImiJsonUtil
                .deserialize(twilioPriceResponse, CountryPricing.class);
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
            twilioMonthlyPriceMap = new HashMap<String, Map<String, String>>();
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
                        String monthlyRental = row[10];
                        Map<String, String> numberTypePriceMap = null;
                        if (twilioMonthlyPriceMap.containsKey(isoCode)) {
                            numberTypePriceMap = twilioMonthlyPriceMap
                                    .get(isoCode);
                            numberTypePriceMap.put(numberType, monthlyRental);
                        } else {
                            numberTypePriceMap = new HashMap<String, String>();
                            numberTypePriceMap.put(numberType, monthlyRental);
                            twilioMonthlyPriceMap.put(isoCode,
                                    numberTypePriceMap);
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
        return numberTypePricingMap;
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
                    IOException {
        String url = TWILIO_COUNTRY_LIST_URL;
        String authHash = BasicAuthUtil.getBasicAuthHash(provider.getAuthId(),
                provider.getApiKey());
        String responseBody;
        Set<com.imi.rest.model.Country> countrySet = new HashSet<com.imi.rest.model.Country>();
        try {
            responseBody = HttpUtil.defaultHttpGetHandler(url, authHash);
        } catch (ImiException e) {
            return countrySet;
        }
        CountryResponse countryResponse = ImiJsonUtil.deserialize(responseBody,
                CountryResponse.class);
        while (countryResponse != null
                && countryResponse.getMeta().getNextPageUrl() != null) {
            String nextPageUrl = countryResponse.getMeta().getNextPageUrl();
            String nextResponseBody = "";
            try {
                nextResponseBody = HttpUtil.defaultHttpGetHandler(nextPageUrl,
                        authHash);
            } catch (ImiException e) {
            }
            CountryResponse countryResponse2 = ImiJsonUtil
                    .deserialize(nextResponseBody, CountryResponse.class);
            countryResponse.addCountries(countryResponse2.getCountries());
        }
        countrySet = countryResponse.getCountries();
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

    public PurchaseResponse purchaseNumber(String number, Provider provider,
            Country country)
                    throws ClientProtocolException, IOException, ImiException {
        String twilioPurchaseUrl = TWILIO_DUMMY_PURCHASE_URL;
        String twilioNumber = "+" + country.getCountryCode().trim()
                + number.trim();
        twilioPurchaseUrl = twilioPurchaseUrl.replace("{number}", twilioNumber);
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("PhoneNumber", twilioNumber);
        String response = HttpUtil.defaultHttpPostHandler(twilioPurchaseUrl,
                requestBody, BasicAuthUtil.getBasicAuthHash(
                        provider.getAuthId(), provider.getApiKey()));
        JSONObject twilioResponse = XML.toJSONObject(response);
        getTwilioPricing(country.getCountryIso(),
                providerService.getTwilioProvider());
        return null;
    }

    public void releaseNumber(String number, Provider provider,
            String countryIsoCode) {

    }

    public Map<String, String> getNumberTypePricingMap() {
        return numberTypePricingMap;
    }

    public void setNumberTypePricingMap(
            Map<String, String> numberTypePricingMap) {
        this.numberTypePricingMap = numberTypePricingMap;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

}
