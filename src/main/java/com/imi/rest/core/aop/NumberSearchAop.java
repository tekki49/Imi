package com.imi.rest.core.aop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.imi.rest.constants.NumberTypeConstants;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.dao.model.Provider;

import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.Meta;
import com.imi.rest.model.Number;
import com.imi.rest.model.NumberResponse;
import com.imi.rest.model.TwilioNumberPrice;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiDataFormatUtils;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class NumberSearchAop implements UrlConstants, ProviderConstants, NumberTypeConstants {

	@Autowired
	ServiceTypeAop serviceTypeAop;

	@Value(value = "${default.markup.value}")
	String defaultMarkup;

	private final static int NEXMO_MAX_THRESHOLD = 100;
	// limit the no of concurrent api calls
	private final static int NEXMO_API_CALL_THRESHOLD = 2;
	// Max no of iterative api calls
	private final static int PLIVO_API_CALL_THRESHOLD = 1;

	private static final Logger LOG = Logger.getLogger(NumberSearchAop.class);

	public void searchTwilioNumbers(Double forexValue, Map<String, TwilioNumberPrice> twilioMonthlyPriceMap,
			Provider provider, ServiceConstants serviceTypeEnum, String countryIsoCode, String numberType,
			String pattern, String index, NumberResponse numberResponse, String markup)
					throws ClientProtocolException, IOException {
		if (!index.equalsIgnoreCase("")) {
			return;
		}
		List<Number> numberSearchList = numberResponse.getObjects() == null ? new ArrayList<Number>()
				: numberResponse.getObjects();
		String twilioPhoneSearchUrl = TWILIO_PHONE_SEARCH_URL;
		String servicesString = NumberCapabiltyAop.generateTwilioCapabilities(serviceTypeEnum);
		String type = "Local";
		if (numberType.equalsIgnoreCase(LANDLINE) || numberType.equalsIgnoreCase(LOCAL)) {
			type = "Local";
			numberType = LANDLINE;
		} else if (numberType.equalsIgnoreCase(MOBILE)) {
			type = "Mobile";
			numberType = MOBILE;

		} else if (numberType.equalsIgnoreCase(TOLLFREE)) {
			type = "Tollfree";
			numberType = TOLLFREE;
		} else if (numberType.equalsIgnoreCase(MULTI)) {
			type = "Local";
			numberType = MULTI;
		}
		twilioPhoneSearchUrl = twilioPhoneSearchUrl.replace("{auth_id}", provider.getAuthId())
				.replace("{country_iso}", countryIsoCode).replace("{services}", servicesString)
				.replace("{pattern}", "*" + pattern.trim() + "*").replace("{type}", type);
		GenericRestResponse restResponse = null;
		if (pattern.equalsIgnoreCase("")) {
			twilioPhoneSearchUrl = twilioPhoneSearchUrl.replace("Contains=**&", "");
		}
		try {
			restResponse = ImiHttpUtil.defaultHttpGetHandler(twilioPhoneSearchUrl,
					ImiBasicAuthUtil.getBasicAuthHash(provider));
		} catch (Exception e) {
			LOG.error(ImiDataFormatUtils.getStackTrace(e));
		}
		if (restResponse != null && restResponse.getResponseCode() == HttpStatus.OK.value()) {
			NumberResponse numberResponseFromTwilo = ImiJsonUtil.deserialize(restResponse.getResponseBody(),
					NumberResponse.class);
			List<Number> twilioNumberList = numberResponseFromTwilo == null ? new ArrayList<Number>()
					: numberResponseFromTwilo.getObjects() == null ? new ArrayList<Number>()
							: numberResponseFromTwilo.getObjects();
			String searchKey = countryIsoCode + "-" + type;
			TwilioNumberPrice numberTypePricing = twilioMonthlyPriceMap.get(searchKey.toUpperCase());
			if (numberTypePricing == null && type.equalsIgnoreCase("Local")) {
				searchKey = countryIsoCode + "-" + "National";
				numberTypePricing = twilioMonthlyPriceMap.get(searchKey.toUpperCase());
			}
			if (numberTypePricing == null) {
				return;
			}
			// String voiceRate = numberTypePricing.get(type.toLowerCase());
			for (Number twilioNumber : twilioNumberList) {
				if (twilioNumber != null) {
					serviceTypeAop.setTwilioServiceType(twilioNumber);
					twilioNumber.setProvider("" + provider.getId());
					twilioNumber.setType(numberType);
					if (twilioNumber.isVoiceEnabled()) {
						String voiceRateInGBP = ImiDataFormatUtils.forexConvert(forexValue,
								getHikedPrice(numberTypePricing.getInboundVoicePrice(), markup));
						twilioNumber.setVoiceRate(voiceRateInGBP);
					}
					if (twilioNumber.isSmsEnabled()) {
						String smsRateInGBP = ImiDataFormatUtils.forexConvert(forexValue,
								getHikedPrice(numberTypePricing.getInboundSmsPrice(), markup));
						twilioNumber.setSmsRate(smsRateInGBP);
					}
					String monthlyRentalRateInGBP = ImiDataFormatUtils.forexConvert(forexValue,
							getHikedPrice(numberTypePricing.getMonthlyRentalRate(), markup));
					twilioNumber.setMonthlyRentalRate(monthlyRentalRateInGBP);
					numberSearchList.add(twilioNumber);
				}
			}
			numberResponse.setObjects(numberSearchList);
		} else {
			LOG.error("Provider details seem to be improper. Please check account details for " + provider.getName());
		}
	}

	public void searchNexmoPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum, String countryIsoCode,
			String numberType, String pattern, List<Number> numberSearchList, int index, NumberResponse numberResponse,
			String previousNexmoIndex, Double forexValue, Map<String, CountryPricing> countryPricingMap, String markup)
					throws ClientProtocolException, IOException {
		// if(numberSearchList.size()>THRESHOLD)return;
		int iterativeApiCalls = 0;
		String nexmoPhoneSearchUrl = NEXMO_PHONE_SEARCH_URL;
		if (serviceTypeEnum.equals(ServiceConstants.ANY)) {
			nexmoPhoneSearchUrl = nexmoPhoneSearchUrl.replace("&features={features}", "");
		}
		nexmoPhoneSearchUrl = nexmoPhoneSearchUrl.replace("{country_iso}", countryIsoCode)
				.replace("{api_key}", provider.getAuthId()).replace("{api_secret}", provider.getApiKey())
				.replace("{pattern}", pattern).replace("{features}", serviceTypeEnum.toString().toUpperCase());
		GenericRestResponse restResponse = null;
		boolean recursive = true;
		String inboundPrice = "inboundRateInEuros";
		String monthlyRate = "monthlyRateInEuros";
		CountryPricing countryPricing = countryPricingMap.get(countryIsoCode);
		while (recursive) {
			String url = nexmoPhoneSearchUrl.replace("{index}", "" + index);
			try {
				restResponse = ImiHttpUtil.defaultHttpGetHandler(url);
			} catch (Exception e) {
				LOG.error(ImiDataFormatUtils.getStackTrace(e));
				recursive = false;
			}
			if (restResponse != null && restResponse.getResponseCode() == HttpStatus.OK.value()) {
				NumberResponse nexmoNumberResponse = ImiJsonUtil.deserialize(
						restResponse.getResponseBody() == null ? "" : restResponse.getResponseBody(),
						NumberResponse.class);
				if (nexmoNumberResponse == null)
					return;
				List<Number> nexmoNumberList = nexmoNumberResponse == null ? new ArrayList<Number>()
						: nexmoNumberResponse.getObjects() == null ? new ArrayList<Number>()
								: nexmoNumberResponse.getObjects();
				for (Number nexmoNumber : nexmoNumberList) {
					if (nexmoNumber != null) {
						serviceTypeAop.setNexmoServiceType(nexmoNumber);
						boolean isValidNumber = isNumberWithExpectedFeatures(nexmoNumber, serviceTypeEnum);
						if (numberType.equalsIgnoreCase(TOLLFREE)) {
							String type = nexmoNumber.getNumberType();
							if (type != null) {
								type = type.replace("-", "");
								if (!type.toUpperCase().contains(TOLLFREE)) {
									isValidNumber = false;
								}
							}
						}
						if (isValidNumber) {
							nexmoNumber.setType(numberType);
							nexmoNumber.setPriceUnit("EUR");
							String type = "";
							if (numberType.equalsIgnoreCase(LANDLINE) || numberType.equalsIgnoreCase(MOBILE)
									|| numberType.equalsIgnoreCase(TOLLFREE)) {
								type = numberType;
							} else if (numberType.equalsIgnoreCase(MULTI)) {
								type = LANDLINE;
							}
							if (nexmoNumber.isSmsEnabled() && type.equals(MOBILE)) {
								String smsRate = ImiDataFormatUtils.forexConvert(forexValue,
										getHikedPrice(getNexmoPrice(countryPricing, type, inboundPrice), markup));
								nexmoNumber.setSmsRate(smsRate);
							}
							if (nexmoNumber.isVoiceEnabled()) {
								String voiceRate = ImiDataFormatUtils.forexConvert(forexValue,
										getHikedPrice(getNexmoPrice(countryPricing, type, inboundPrice), markup));
								nexmoNumber.setVoiceRate(voiceRate);
							}
							nexmoNumber.setMonthlyRentalRate(ImiDataFormatUtils.forexConvert(forexValue,
									getHikedPrice(getNexmoPrice(countryPricing, type, monthlyRate), markup)));
							nexmoNumber.setProvider("" + provider.getId());
							numberSearchList.add(nexmoNumber);
						}
					}
				}
				if (nexmoNumberResponse.getCount() - index * 100 > 0) {
					if (numberSearchList.size() < NEXMO_MAX_THRESHOLD) {
						index++;
					} else {
						Meta meta = numberResponse.getMeta() == null ? new Meta() : numberResponse.getMeta();
						String nextNexmoIndex = "" + (index + 1);
						meta.setPreviousNexmoIndex(previousNexmoIndex);
						meta.setNextNexmoIndex(nextNexmoIndex);
						numberResponse.setMeta(meta);
						recursive = false;
					}
					if (iterativeApiCalls < NEXMO_API_CALL_THRESHOLD) {
						iterativeApiCalls++;
					} else {
						recursive = false;
					}
				} else {
					recursive = false;
				}
			} else {
				recursive = false;
				LOG.error("Exception occured while searching for service provider " + provider.getName() + " url is "
						+ url);
			}
		}
	}

	public void searchPlivoPhoneNumbers(Provider provider, ServiceConstants serviceTypeEnum, String countryIsoCode,
			String numberTypePlivoApi, String pattern, List<Number> numberSearchList, int offset,
			NumberResponse numberResponse, Double forexValue, String numberType, String markup)
					throws ClientProtocolException, IOException {
		String plivioPhoneSearchUrl = PLIVO_PHONE_SEARCH_URL;
		if (serviceTypeEnum.equals(ServiceConstants.ANY)) {
			plivioPhoneSearchUrl = plivioPhoneSearchUrl.replace("&services={services}", "");
		}
		plivioPhoneSearchUrl = plivioPhoneSearchUrl.replace("{auth_id}", provider.getAuthId())
				.replace("{country_iso}", countryIsoCode).replace("{type}", numberTypePlivoApi)
				.replace("{services}", serviceTypeEnum.toString()).replace("{pattern}", "*" + pattern + "*");
		boolean recursive = true;
		int counter = 0;
		while (recursive) {
			String phoneSearchUrl = plivioPhoneSearchUrl;
			if (offset > 0) {
				phoneSearchUrl = phoneSearchUrl + "&offset=" + offset;
			}
			GenericRestResponse restResponse = null;
			try {
				restResponse = ImiHttpUtil.defaultHttpGetHandler(plivioPhoneSearchUrl,
						ImiBasicAuthUtil.getBasicAuthHash(provider));
			} catch (Exception e) {
				LOG.error(ImiDataFormatUtils.getStackTrace(e));
				recursive = false;
			}

			if (restResponse != null && restResponse.getResponseCode() == HttpStatus.OK.value()) {
				NumberResponse numberResponseFromPlivo = ImiJsonUtil.deserialize(
						restResponse.getResponseBody() == null ? "" : restResponse.getResponseBody(),
						NumberResponse.class);
				List<Number> plivioNumberList = numberResponseFromPlivo == null ? new ArrayList<Number>()
						: numberResponseFromPlivo.getObjects() == null ? new ArrayList<Number>()
								: numberResponseFromPlivo.getObjects();
				for (Number plivioNumber : plivioNumberList) {
					if (plivioNumber != null) {
						if (isNumberWithExpectedFeatures(plivioNumber, serviceTypeEnum)) {
							plivioNumber.setProvider("" + provider.getId());
							serviceTypeAop.setPlivoServiceType(plivioNumber);
							String monthlyRentalRateInGBP = ImiDataFormatUtils.forexConvert(forexValue,
									getHikedPrice(plivioNumber.getMonthlyRentalRate(), markup));
							plivioNumber.setMonthlyRentalRate(monthlyRentalRateInGBP);

							if (plivioNumber.isVoiceEnabled()) {
								String voiceRateInGBP = ImiDataFormatUtils.forexConvert(forexValue,
										getHikedPrice(plivioNumber.getVoiceRate(), markup));
								plivioNumber.setVoiceRate(voiceRateInGBP);
							}
							if (plivioNumber.isSmsEnabled()) {
								String smsRateInGBP = ImiDataFormatUtils.forexConvert(forexValue,
										getHikedPrice(plivioNumber.getSmsRate(), markup));
								plivioNumber.setSmsRate(smsRateInGBP);
							}

							plivioNumber.setType(numberType);
							numberSearchList.add(plivioNumber);
						}
					}
				}
				if (numberResponseFromPlivo.getMeta() != null && numberResponseFromPlivo.getMeta().getNext() != null
						&& !numberResponseFromPlivo.getMeta().getNext().equals("")) {
					if (numberSearchList.size() < PLIVO_API_CALL_THRESHOLD) {
						Meta meta = numberResponse.getMeta() == null ? new Meta() : numberResponse.getMeta();
						String previousPlivoIndex = meta.getNextPlivoIndex();
						previousPlivoIndex = "" + offset;
						String nextPlivoIndex = null;
						nextPlivoIndex = "" + (numberResponseFromPlivo.getMeta().getOffset()
								+ numberResponseFromPlivo.getMeta().getLimit());
						meta.setPreviousPlivoIndex(previousPlivoIndex);
						meta.setNextPlivoIndex(nextPlivoIndex);
						offset = numberResponseFromPlivo.getMeta().getOffset()
								+ numberResponseFromPlivo.getMeta().getLimit();
						numberResponse.setMeta(meta);
					} else {
						recursive = false;
					}
				}
			} else {
				recursive = false;
				LOG.error("Exception occured while searching for service provider " + provider.getName());
			}
			counter++;
			if (PLIVO_API_CALL_THRESHOLD <= counter) {
				recursive = false;
			}
		}
	}

	private String getHikedPrice(String input, String markup) {
		return ImiDataFormatUtils.getHikedPrice(input, markup == null ? defaultMarkup : markup);
	}

	private String getNexmoPrice(CountryPricing countryPricing, String numberType, String priceType) {
		return countryPricing.getPricing().get(numberType).get(priceType);
	}

	private boolean isNumberWithExpectedFeatures(Number number, ServiceConstants serviceTypeEnum) {
		boolean isValid = false;
		if (serviceTypeEnum.equals(ServiceConstants.ANY)) {
			isValid = true;
		} else {
			List<String> expectedFeatures = new ArrayList<String>();
			if (serviceTypeEnum.equals(ServiceConstants.VOICE)) {
				expectedFeatures.add(ServiceConstants.VOICE.toString().toUpperCase());
			} else if (serviceTypeEnum.equals(ServiceConstants.SMS)) {
				expectedFeatures.add(ServiceConstants.SMS.toString().toUpperCase());
			} else if (serviceTypeEnum.equals(ServiceConstants.BOTH)) {
				expectedFeatures.add(ServiceConstants.SMS.toString().toUpperCase());
				expectedFeatures.add(ServiceConstants.VOICE.toString().toUpperCase());
			}
			List<String> numberFeatures = new ArrayList<String>();
			if (number.isSmsEnabled()) {
				numberFeatures.add(ServiceConstants.SMS.toString().toUpperCase());
			}
			if (number.isVoiceEnabled()) {
				numberFeatures.add(ServiceConstants.VOICE.toString().toUpperCase());
			}
			if (expectedFeatures.size() == numberFeatures.size()) {
				isValid = true;
				for (String numberFeature : numberFeatures) {
					if (!expectedFeatures.contains(numberFeature)) {
						isValid = false;
					}
				}
			}
		}
		return isValid;
	}
}
