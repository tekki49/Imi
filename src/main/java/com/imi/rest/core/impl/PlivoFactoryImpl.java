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

import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.imi.rest.dao.ForexDao;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.exception.ImiException;
import com.imi.rest.model.ApplicationResponse;
import com.imi.rest.model.BalanceResponse;
import com.imi.rest.model.Country;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.PlivioPurchaseResponse;
import com.imi.rest.model.PlivoAccountResponse;
import com.imi.rest.model.PlivoPurchaseResponse;
import com.imi.rest.model.PurchaseResponse;
import com.imi.rest.service.CountrySearchService;
import com.imi.rest.service.ForexService;
import com.imi.rest.util.BasicAuthUtil;
import com.imi.rest.util.DataFormatUtils;
import com.imi.rest.util.HttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class PlivoFactoryImpl implements NumberSearch, CountrySearch, PurchaseNumber, UrlConstants, ProviderConstants,
		NumberTypeConstants {

	private static final String PLIVO_CSV_FILE_PATH = "/plivo_inbound_rates.csv";
	// max no of numbers to be obtained
	private static final Logger LOG = Logger.getLogger(CountrySearchService.class);
	private Double forexValue;
	@Autowired
	ForexService forexService;

	@Override
	public void searchPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum, String countryIsoCode,
			String numberType, String pattern, String index, NumberResponse numberResponse)
					throws ClientProtocolException, IOException, ImiException {
		List<Number> numberSearchList = numberResponse.getObjects() == null ? new ArrayList<Number>()
				: numberResponse.getObjects();
		String type = "any";
		if (numberType.equalsIgnoreCase(LANDLINE)) {
			type = "fixed";
		} else if (numberType.equalsIgnoreCase(MOBILE)) {
			type = "mobile ";
		} else if (numberType.equalsIgnoreCase(TOLLFREE)) {
			type = "tollfree";
		} else if (numberType.equalsIgnoreCase(LOCAL)) {
			type = "fixed";
		}
		int offset = 0;
		if (!index.equalsIgnoreCase("FIRST")) {
			offset = Integer.parseInt(index);
		} else {
			Meta meta = numberResponse.getMeta() == null ? new Meta() : numberResponse.getMeta();
			meta.setPreviousPlivoIndex("FIRST");
			numberResponse.setMeta(meta);
		}

		searchPhoneNumbers(provider, serviceTypeEnum, countryIsoCode, type, pattern, numberSearchList, offset,
				numberResponse);
	}

	void searchPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum, String countryIsoCode,
			String numberType, String pattern, List<Number> numberSearchList, int offset, NumberResponse numberResponse)
					throws ClientProtocolException, IOException, ImiException {
		String plivioPhoneSearchUrl = PLIVO_PHONE_SEARCH_URL;
		plivioPhoneSearchUrl = plivioPhoneSearchUrl.replace("{auth_id}", provider.getAuthId())
				.replace("{country_iso}", countryIsoCode).replace("{type}", numberType)
				.replace("{services}", serviceTypeEnum.toString()).replace("{pattern}", "*" + pattern + "*");
		if (offset > 0) {
			plivioPhoneSearchUrl = plivioPhoneSearchUrl + "&offset=" + offset;
		}
		String response = "";
		try {
			response = HttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
					BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()));
		} catch (ImiException e) {
			return;
		}
		NumberResponse numberResponseFromPlivo = ImiJsonUtil.deserialize(response, NumberResponse.class);
		List<Number> plivioNumberList = numberResponseFromPlivo == null ? new ArrayList<Number>()
				: numberResponseFromPlivo.getObjects() == null ? new ArrayList<Number>()
						: numberResponseFromPlivo.getObjects();
		if(forexValue==0){
			forexValue=forexService.getForexValueByName("USD_GBP").getValue();
		}		
		for (Number plivioNumber : plivioNumberList) {
			if (plivioNumber != null) {
				plivioNumber.setProvider(PLIVO);
				setServiceType(plivioNumber);
				plivioNumber.setPriceUnit("USD");
				String monthlyRentalRateInGBP = DataFormatUtils.forexConvert(forexValue,
						plivioNumber.getMonthlyRentalRate());
				plivioNumber.setMonthlyRentalRate(monthlyRentalRateInGBP);
				String voiceRateInGBP = DataFormatUtils.forexConvert(forexValue,
						plivioNumber.getVoiceRate());
				plivioNumber.setVoiceRate(voiceRateInGBP);
				if (numberType.equalsIgnoreCase("fixed") || numberType.equalsIgnoreCase("local")) {
					numberType = "Landline";
				}
				plivioNumber.setType(numberType.toLowerCase());
				numberSearchList.add(plivioNumber);
			}
		}

		if (numberResponseFromPlivo.getMeta() != null && numberResponseFromPlivo.getMeta().getNext() != null
				&& !numberResponseFromPlivo.getMeta().getNext().equals("")) {
			Meta meta = numberResponse.getMeta() == null ? new Meta() : numberResponse.getMeta();
			String previousPlivoIndex = meta.getNextPlivoIndex();
			previousPlivoIndex = "" + offset;
			String nextPlivoIndex = null;
			nextPlivoIndex = ""
					+ (numberResponseFromPlivo.getMeta().getOffset() + numberResponseFromPlivo.getMeta().getLimit());
			meta.setPreviousPlivoIndex(previousPlivoIndex);
			meta.setNextPlivoIndex(nextPlivoIndex);
			numberResponse.setMeta(meta);
		}

		numberResponse.setObjects(numberSearchList);
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
	public Set<Country> importCountries()
			throws FileNotFoundException, JsonParseException, JsonMappingException, IOException {
		Set<Country> countrySet = new TreeSet<Country>();
		String line = "";
		String splitBy = ",";
		BufferedReader reader = null;
		try {
			InputStream in = getClass().getResourceAsStream(PLIVO_CSV_FILE_PATH);
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

	public void releaseNumber(String number, Provider provider, String countryIsoCode)
			throws ClientProtocolException, IOException {
		String plivioReleaseurl = PLIVO_RELEASE_URL;
		String plivioNumber = number.trim() + countryIsoCode.trim();
		plivioReleaseurl = plivioReleaseurl.replace("{number}", plivioNumber).replace("{auth_id}",
				provider.getAuthId());
		String response = HttpUtil.defaultHttpDeleteHandler(plivioReleaseurl, new HashMap<String, String>(),
				BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()), null);
		// TODO need to handle release appropriately
	}

	public BalanceResponse checkBalance(Provider provider) throws ClientProtocolException, IOException {
		String plivoAccountBalanceurl = PLIVO_ACCOUNT_BALANCE_URL;
		plivoAccountBalanceurl = plivoAccountBalanceurl.replace("{auth_id}", provider.getAuthId());
		BalanceResponse balanceResponse = new BalanceResponse();
		try {
			String response = HttpUtil.defaultHttpGetHandler(plivoAccountBalanceurl,
					BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()));
			PlivoAccountResponse plivoAccountResponse = ImiJsonUtil.deserialize(response, PlivoAccountResponse.class);
			balanceResponse.setAccountBalance(plivoAccountResponse.getCash_credits());
			if (balanceResponse.getAccountBalance() != null) {
				balanceResponse.setAccountBalance(balanceResponse.getAccountBalance() + " USD");
			}
		} catch (ImiException e) {
			// TODO need to validate the response
		}
		return balanceResponse;
	}

	@Override
	public PurchaseResponse purchaseNumber(String number, String numberType, Provider provider,
			com.imi.rest.dao.model.Country country) throws ClientProtocolException, IOException, ImiException {
		PurchaseResponse purchaseResponse = new PurchaseResponse();
		String plivoPurchaseUrl = PLIVO_PURCHASE_URL.replace("{auth_id}", provider.getAuthId()).replace("{number}",
				number);
		NumberResponse numberResponse = new NumberResponse();
		searchPhoneNumbers(provider, null, country.getCountryIso(), numberType, number, "0", numberResponse);
		String response = HttpUtil.defaultHttpPostHandler(plivoPurchaseUrl, new HashMap<String, String>(),
				BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()),
				ContentType.APPLICATION_JSON.getMimeType());
		Number numberDetails = numberResponse.getObjects() == null ? null : numberResponse.getObjects().get(0);
		PlivoPurchaseResponse plivoPurchaseResponse = ImiJsonUtil.deserialize(response, PlivoPurchaseResponse.class);
		if (plivoPurchaseResponse.getError() != null) {
			throw new ImiException(
					"error occured for number provided " + number + " Error :" + plivoPurchaseResponse.getError());
		}
		if (plivoPurchaseResponse.getStatus().equals("fulfilled")) {
			purchaseResponse.setAddressRequired(false);
		} else {
			purchaseResponse.setAddressRequired(true);
		}
		if (plivoPurchaseResponse.getNumbers().get(0).getStatus().equalsIgnoreCase("Success")) {
			purchaseResponse.setStatus("Success");
		}
		purchaseResponse.setNumber(number);
		purchaseResponse.setNumberType(numberType);
		purchaseResponse.setMonthlyRentalRate(numberDetails.getMonthlyRentalRate());
		purchaseResponse.setSetUpRate(numberDetails.getSetUpRate());
		purchaseResponse.setSmsRate(numberDetails.getSmsRate());
		purchaseResponse.setVoicePrice(numberDetails.getVoiceRate());
		purchaseResponse.setEffectiveDate("");
		purchaseResponse.setResourceManagerId(0);
		purchaseResponse.setCountryProviderId(country.getId());
		return purchaseResponse;
	}

	private ApplicationResponse createApplication(ApplicationResponse plivoApplication, Provider provider) {
		String plivoCreateApplicationurl = PLIVO_APPLICATION_CREATE_URL;
		plivoCreateApplicationurl = plivoCreateApplicationurl.replace("{auth_id}", provider.getAuthId());
		ApplicationResponse plivoApplicationResponse = new ApplicationResponse();
		try {
			Map<String, String> requestBody = new HashMap<String, String>();

			if (plivoApplication.getVoiceUrl() != null) {
				requestBody.put("answer_url", plivoApplication.getVoiceUrl());
			}
			if (plivoApplication.getApp_name() != null) {
				requestBody.put("app_name", plivoApplication.getApp_name());
			}
			if (plivoApplication.getVoiceMethod() != null) {
				requestBody.put("answer_method", plivoApplication.getVoiceMethod());
			}
			if (plivoApplication.getStatusCallback() != null) {
				requestBody.put("hangup_url", plivoApplication.getStatusCallback());
			}
			if (plivoApplication.getStatusCallbackMethod() != null) {
				requestBody.put("hangup_method", plivoApplication.getStatusCallbackMethod());
			}
			if (plivoApplication.getVoiceFallback() != null) {
				requestBody.put("fallback_answer_url", plivoApplication.getVoiceFallback());
			}
			if (plivoApplication.getVoiceFallbackMethod() != null) {
				requestBody.put("fallback_method", plivoApplication.getVoiceFallbackMethod());
			}
			if (plivoApplication.getSmsUrl() != null) {
				requestBody.put("message_url", plivoApplication.getSmsUrl());
			}
			if (plivoApplication.getSmsMethod() != null) {
				requestBody.put("message_method", plivoApplication.getSmsMethod());
			}
			if (plivoApplication.getDefault_number_app() != null) {
				requestBody.put("default_number_app", plivoApplication.getDefault_number_app().toString());
			}
			if (plivoApplication.getDefault_endpoint_app() != null) {
				requestBody.put("default_endpoint_app", plivoApplication.getDefault_endpoint_app().toString());
			}
			String response = HttpUtil.defaultHttpPostHandler(plivoCreateApplicationurl, requestBody,
					BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()),
					ContentType.APPLICATION_JSON.getMimeType());
			plivoApplicationResponse = ImiJsonUtil.deserialize(response, ApplicationResponse.class);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return plivoApplicationResponse;
	}

	private ApplicationResponse updateApplication(ApplicationResponse plivoApplication, Provider provider)
			throws ClientProtocolException, IOException {
		String plivoApplicationUpdateUrl = PLIVO_APPLICATION_UPDATE_URL;
		plivoApplicationUpdateUrl = plivoApplicationUpdateUrl.replace("{auth_id}", provider.getAuthId())
				.replace("{app_id}", plivoApplication.getApp_id());
		ApplicationResponse currentPlivoApplication = getApplication(plivoApplication.getApp_id(), provider);
		Map<String, String> requestBody = new HashMap<String, String>();
		if (plivoApplication.getVoiceUrl() != null
				&& plivoApplication.getVoiceUrl() != currentPlivoApplication.getVoiceUrl()) {
			requestBody.put("answer_url", plivoApplication.getVoiceUrl());
		}
		if (plivoApplication.getApp_name() != null
				&& plivoApplication.getApp_name() != currentPlivoApplication.getApp_name()) {
			requestBody.put("app_name", plivoApplication.getApp_name());
		}
		if (plivoApplication.getVoiceMethod() != null
				&& plivoApplication.getVoiceMethod() != currentPlivoApplication.getVoiceMethod()) {
			requestBody.put("answer_method", plivoApplication.getVoiceMethod());
		}
		if (plivoApplication.getStatusCallback() != null) {
			requestBody.put("hangup_url", plivoApplication.getStatusCallback());
		}
		if (plivoApplication.getStatusCallbackMethod() != null) {
			requestBody.put("hangup_method", plivoApplication.getStatusCallbackMethod());
		}
		if (plivoApplication.getVoiceFallback() != null) {
			requestBody.put("fallback_answer_url", plivoApplication.getVoiceFallback());
		}
		if (plivoApplication.getVoiceFallbackMethod() != null) {
			requestBody.put("fallback_method", plivoApplication.getVoiceFallbackMethod());
		}
		if (plivoApplication.getSmsUrl() != null) {
			requestBody.put("message_url", plivoApplication.getSmsUrl());
		}
		if (plivoApplication.getSmsMethod() != null) {
			requestBody.put("message_method", plivoApplication.getSmsMethod());
		}
		if (plivoApplication.getDefault_number_app() != null) {
			requestBody.put("default_number_app", plivoApplication.getDefault_number_app().toString());
		}
		if (plivoApplication.getDefault_endpoint_app() != null) {
			requestBody.put("default_endpoint_app", plivoApplication.getDefault_endpoint_app().toString());
		}
		String response = HttpUtil.defaultHttpPostHandler(plivoApplicationUpdateUrl, requestBody,
				BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()),
				ContentType.APPLICATION_JSON.getMimeType());
		PlivioPurchaseResponse plivoResponse = ImiJsonUtil.deserialize(response, PlivioPurchaseResponse.class);
		if (plivoResponse.getMessage().equals("changed")) {
			plivoApplication = getApplication(currentPlivoApplication.getApp_id(), provider);
		} else {
			// throw exception saying parameters sent were wrong
		}
		return plivoApplication;
	}

	private ApplicationResponse getApplication(String appId, Provider provider) {
		String plivoApplicationUpdateUrl = PLIVO_APPLICATION_UPDATE_URL;
		plivoApplicationUpdateUrl = plivoApplicationUpdateUrl.replace("{auth_id}", provider.getAuthId())
				.replace("{app_id}", appId);
		ApplicationResponse plivoApplicationResponse = new ApplicationResponse();
		try {
			String response = HttpUtil.defaultHttpGetHandler(plivoApplicationUpdateUrl,
					BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()));
			plivoApplicationResponse = ImiJsonUtil.deserialize(response, ApplicationResponse.class);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImiException e) {
			e.printStackTrace();
		}
		return plivoApplicationResponse;
	}

	public ApplicationResponse updateNumber(String number, ApplicationResponse applicationResponsetoModify,
			Provider provider) {
		String plivoNumberUpdateUrl = PLIVO_RELEASE_URL;
		plivoNumberUpdateUrl = plivoNumberUpdateUrl.replace("{number}", number).replace("{auth_id}",
				provider.getAuthId());
		PlivioPurchaseResponse plivioPurchaseResponse = new PlivioPurchaseResponse();
		ApplicationResponse plivoApplicationResponse = new ApplicationResponse();
		try {
			plivoApplicationResponse = getApplicationByNumber(number, provider);
			if (plivoApplicationResponse != null) {
				plivoApplicationResponse = updateApplication(applicationResponsetoModify, provider);
			} else {
				plivoApplicationResponse = createApplication(applicationResponsetoModify, provider);
			}
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("app_id", plivoApplicationResponse.getApp_id());
			String response = HttpUtil.defaultHttpPostHandler(plivoNumberUpdateUrl, requestBody,
					BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()),
					ContentType.APPLICATION_JSON.getMimeType());
			plivioPurchaseResponse = ImiJsonUtil.deserialize(response, PlivioPurchaseResponse.class);
			if (!plivioPurchaseResponse.getMessage().equals("changed")) {
				String message = "application created successfully, but not assigned to the upadted number";
				ImiException e = new ImiException("", message);
				throw e;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImiException e1) {
			e1.printStackTrace();
		}
		return plivoApplicationResponse;
	}

	private ApplicationResponse getApplicationByNumber(String number, Provider provider) {
		String plivoNumberGetUrl = PLIVO_RELEASE_URL;
		plivoNumberGetUrl = plivoNumberGetUrl.replace("{number}", number).replace("{auth_id}", provider.getAuthId());
		ApplicationResponse plivoApplicationResponse = new ApplicationResponse();
		try {
			String response = HttpUtil.defaultHttpGetHandler(plivoNumberGetUrl,
					BasicAuthUtil.getBasicAuthHash(provider.getAuthId(), provider.getApiKey()));
			Number numberObj = ImiJsonUtil.deserialize(response, Number.class);
			String app_id = numberObj.getApplication()
					.substring(numberObj.getApplication().lastIndexOf("Application/") + 12,
							numberObj.getApplication().length())
					.trim();
			if (app_id.length() > 0) {
				plivoApplicationResponse = getApplication(app_id, provider);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ImiException e) {
			e.printStackTrace();
		}
		return plivoApplicationResponse;
	}

}
