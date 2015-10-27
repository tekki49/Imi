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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.core.CountrySearch;
import com.imi.rest.core.NumberSearch;
import com.imi.rest.model.Country;
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.HttpUtil;

@Component
public class NexmoSearchImpl implements NumberSearch, CountrySearch,
        UrlConstants, ProviderConstants {

    private String nexmoPricingResponse;

    @Override
    public List<Number> searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern)
                    throws ClientProtocolException, IOException {
        List<Number> phoneSearchResult = new ArrayList<Number>();
        searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
                phoneSearchResult, Integer.MIN_VALUE, 1);
        return phoneSearchResult;
    }

    void searchPhoneNumbers(ServiceConstants serviceTypeEnum,
            String countryIsoCode, String numberType, String pattern,
            List<Number> phoneSearchResult, int count, int index)
                    throws ClientProtocolException, IOException {
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
                .replace("{api_key}", "a5eb8aa1")
                .replace("{api_secret}", "b457a519")
                .replace("{pattern}", pattern)
                .replace("{features}", serviceTypeEnum.toString().toUpperCase())
                .replace("{index}", "" + index);
        String response = HttpUtil.defaultHttpGetHandler(nexmoPhoneSearchUrl);
        ObjectMapper mapper = new ObjectMapper();
        NumberResponse numberResponse = mapper.readValue(response,
                NumberResponse.class);
        if (numberResponse == null)
            return;
        List<Number> nexmoNumberList = numberResponse == null
                ? new ArrayList<Number>()
                : numberResponse.getObjects() == null ? new ArrayList<Number>()
                        : numberResponse.getObjects();
        if (getNexmoPricingResponse() == null) {
            setNexmoPricingResponse(getNexmoPricing("/nexmo_pricing.xls"));
        }
        List<CountryPricing> countryPricingList = mapper.readValue(
                nexmoPricingResponse,
                mapper.getTypeFactory().constructCollectionType(List.class,
                        CountryPricing.class));
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
              //TODO- need to check the authenticity of this part
                if (nexmoNumber.getNumberType()
                        .equalsIgnoreCase("mobile-lvn")) {
                    nexmoNumber.setType("mobile");
                }
                nexmoNumber.setMonthlyRentalRate(countryPricing.getPricing()
                        .get(type).get("setUpRateInEuros"));
                nexmoNumber.setVoiceRate(countryPricing.getPricing().get(type)
                        .get("voiceRateInEuros"));
                nexmoNumber.setCountry(countryPricing.getCountry());
                nexmoNumber.setProvider(NEXMO);
                phoneSearchResult.add(nexmoNumber);
            }
        }
        count = numberResponse.getCount();
        searchPhoneNumbers(serviceTypeEnum, countryIsoCode, numberType, pattern,
                phoneSearchResult, count, index);
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
        Set<Country> countriesSet = new HashSet<Country>();
        if (getNexmoPricingResponse() == null) {
            setNexmoPricingResponse(getNexmoPricing("/nexmo_pricing.xls"));
        }
        ObjectMapper mapper = new ObjectMapper();
        List<CountryPricing> countryPricingList = mapper.readValue(
                getNexmoPricingResponse(),
                mapper.getTypeFactory().constructCollectionType(List.class,
                        CountryPricing.class));
        for (CountryPricing countryPricing : countryPricingList) {
            Country country = new Country();
            country.setCountry(countryPricing.getCountry());
            country.setIsoCountry(countryPricing.getCountryIsoCode());
            countriesSet.add(country);
        }
        return countriesSet;
    }

    public void purchaseNumber(String number, String provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        String nexmoPurchaseUrl = NEXMO_PURCHASE_URL;
        String nexmoNumber = number.trim() + countryIsoCode.trim();
        nexmoPurchaseUrl = nexmoPurchaseUrl.replace("{api_key}", "a5eb8aa1")
                .replace("{api_secret}", "b457a519").replace("{country}", "")
                .replace("{msisdn}", nexmoNumber);
        ObjectMapper mapper = new ObjectMapper();
        String response = HttpUtil.defaultHttpGetHandler(nexmoPurchaseUrl);
    }

    public void releaseNumber(String number, String provider,
            String countryIsoCode) throws ClientProtocolException, IOException {
        String nexmoReleaseUrl = NEXMO_RELEASE_URL;
        String nexmoNumber = number.trim() + countryIsoCode.trim();
        nexmoReleaseUrl = nexmoReleaseUrl.replace("{api_key}", "a5eb8aa1")
                .replace("{api_secret}", "b457a519").replace("{country}", "")
                .replace("{msisdn}", nexmoNumber);
        ObjectMapper mapper = new ObjectMapper();
        String response = HttpUtil.defaultHttpGetHandler(nexmoReleaseUrl,
                BasicAuthUtil.getBasicAuthHash(PLIVIO));
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
        ObjectMapper objectMapper = new ObjectMapper();
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
            priceMap.put("setUpRateInEuros", row.getCell(1).toString());
            priceMap.put("voiceRateInEuros", row.getCell(2).toString());
            pricing.put("normal", priceMap);
            priceMap = new HashMap<String, String>();
            priceMap.put("setUpRateInEuros", row.getCell(3).toString());
            priceMap.put("voiceRateInEuros", row.getCell(4).toString());
            pricing.put("tollfree", priceMap);
            countryPricing.setPricing(pricing);
            nexmoPricingList.add(countryPricing);
        }
        response += objectMapper.writeValueAsString(nexmoPricingList);
        in.close();
        workbook.close();
        return response;
    }
}
