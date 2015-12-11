package com.imi.rest.core.aop;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.dao.model.Provider;

import com.imi.rest.model.Country;
import com.imi.rest.model.CountryResponse;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class ImportCountryAop implements UrlConstants, ProviderConstants {

    @Value(value = "${config.twilio.price.sheet.location}")
    private String twilioPriceFilePath;
    @Value(value = "${config.nexmo.price.sheet.location}")
    private String nexmoPriceFilePath;
    @Value(value = "${config.plivo.price.sheet.location}")
    private String plivoPriceFilePath;

    private static final Logger LOG = Logger.getLogger(ImportCountryAop.class);

    public Set<com.imi.rest.model.Country> importTwilioCountriesByUrl(
            Provider provider) throws JsonParseException, JsonMappingException,
            IOException {
        String url = TWILIO_COUNTRY_LIST_URL;
        String authHash = ImiBasicAuthUtil.getBasicAuthHash(provider);
        GenericRestResponse restResponse;
        Set<com.imi.rest.model.Country> countrySet = new HashSet<com.imi.rest.model.Country>();
        restResponse = ImiHttpUtil.defaultHttpGetHandler(url, authHash);
        if (restResponse.getResponseCode() == HttpStatus.OK.value()) {
            CountryResponse countryResponse = ImiJsonUtil.deserialize(
                    restResponse.getResponseBody() == null ? "" : restResponse
                            .getResponseBody(), CountryResponse.class);
            while (countryResponse != null
                    && countryResponse.getMeta().getNextPageUrl() != null) {
                String nextPageUrl = countryResponse.getMeta().getNextPageUrl();
                GenericRestResponse nextRestResponse = null;
                nextRestResponse = ImiHttpUtil.defaultHttpGetHandler(
                        nextPageUrl, authHash);
                if (nextRestResponse.getResponseCode() == HttpStatus.OK.value()) {
                    CountryResponse countryResponse2 = ImiJsonUtil.deserialize(
                            nextRestResponse.getResponseBody() == null ? ""
                                    : nextRestResponse.getResponseBody(),
                            CountryResponse.class);
                    countryResponse.addCountries(countryResponse2
                            .getCountries());
                }
            }
            countrySet = countryResponse.getCountries();
        }
        return countrySet;
    }

    public Set<com.imi.rest.model.Country> importTwilioCountries(
            Map<String, Map<String, String>> providerCapabilities)
            throws JsonParseException, JsonMappingException, IOException {
        Map<String, String> twilioCapabilties = providerCapabilities
                .get(TWILIO);
        Set<com.imi.rest.model.Country> countrySet = new TreeSet<com.imi.rest.model.Country>();
        String line = "";
        String splitBy = ",";
        BufferedReader reader = null;
        try {
            InputStream in = getClass()
                    .getResourceAsStream(twilioPriceFilePath);
            if (in == null) {
                in = new FileInputStream(twilioPriceFilePath);
            }
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
                    String capabilities = row[3].replace(" ", "");
                    if (twilioCapabilties.get(country.getCountry()) != null) {
                        capabilities += ("," + twilioCapabilties.get(country
                                .getCountry()));
                    }
                    twilioCapabilties.put(country.getCountry(), capabilities);
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

    public Set<Country> importNexmoCountries(
            Map<String, Map<String, String>> providerCapabilities)
            throws FileNotFoundException, JsonParseException,
            JsonMappingException, IOException {
        Map<String, String> nexmoCapabilties = providerCapabilities.get(NEXMO);
        Set<Country> countrySet = new HashSet<Country>();
        InputStream in = getClass().getResourceAsStream(nexmoPriceFilePath);
        if (in == null) {
            in = new FileInputStream(nexmoPriceFilePath);
        }
        HSSFWorkbook workbook = new HSSFWorkbook(in);

        HSSFSheet sheet = workbook.getSheetAt(3);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String countryName = row.getCell(0).toString().split(" - ")[1];
            String capabilities = "";
            if (row.getCell(1).toString() != null
                    && !row.getCell(1).toString().trim().equals("")) {
                capabilities += "Voice";
            }
            if (row.getCell(3).toString() != null
                    && !row.getCell(3).toString().trim().equals("")) {
                capabilities += ",Tollfree";
            }
            if (nexmoCapabilties.get(countryName) != null) {
                capabilities += ("," + nexmoCapabilties.get(countryName));
            }
            nexmoCapabilties.put(countryName, capabilities);
        }
        sheet = workbook.getSheetAt(2);
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String countryName = row.getCell(0).toString().split(" - ")[1];
            String capabilities = "";
            if (row.getCell(1).toString() != null
                    && !row.getCell(1).toString().trim().equals("")) {
                capabilities += "Mobile";
            }
            if (nexmoCapabilties.get(countryName) != null) {
                capabilities += ("," + nexmoCapabilties.get(countryName));
            }
            nexmoCapabilties.put(countryName, capabilities);
        }

        sheet = workbook.getSheetAt(0);
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

    public Set<Country> importPlivoCountries(
            Map<String, Map<String, String>> providerCapabilities)
            throws FileNotFoundException, JsonParseException,
            JsonMappingException, IOException {
        Map<String, String> plivoCapabilties = providerCapabilities.get(PLIVO);
        Set<Country> countrySet = new TreeSet<Country>();
        String line = "";
        String splitBy = ",";
        BufferedReader reader = null;
        try {
            InputStream in = getClass().getResourceAsStream(plivoPriceFilePath);
            if (in == null) {
                in = new FileInputStream(plivoPriceFilePath);
            }
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
                    String capabilities = row[4].replace(" ", "");
                    if (plivoCapabilties.get(country.getCountry()) != null) {
                        capabilities += ("," + plivoCapabilties.get(country
                                .getCountry()));
                    }
                    plivoCapabilties.put(country.getCountry(), capabilities);
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
}
