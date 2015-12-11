package com.imi.rest.core.aop;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.imi.rest.constants.NumberTypeConstants;
import com.imi.rest.constants.ProviderConstants;
import com.imi.rest.constants.UrlConstants;
import com.imi.rest.dao.model.Provider;
import com.imi.rest.model.CountryPricing;
import com.imi.rest.model.GenericRestResponse;
import com.imi.rest.model.InboundCallPrice;
import com.imi.rest.model.TwilioNumberPrice;
import com.imi.rest.util.ImiBasicAuthUtil;
import com.imi.rest.util.ImiHttpUtil;
import com.imi.rest.util.ImiJsonUtil;

@Component
public class PricingAop implements UrlConstants, ProviderConstants, NumberTypeConstants {

	@Value(value = "${config.twilio.price.sheet.location}")
	private String twilioPriceFilePath;
	@Value(value = "${config.nexmo.price.sheet.location}")
	private String nexmoPriceFilePath;
	@Value(value = "${config.plivo.price.sheet.location}")
	private String plivoPriceFilePath;
	private static final Logger LOG = Logger.getLogger(PricingAop.class);

	public Map<String, TwilioNumberPrice> getTwilioPricing(String countryIsoCode, Provider provider)
			throws ClientProtocolException, IOException {
		Map<String, TwilioNumberPrice> twilioMonthlyPriceMap = new HashMap<String, TwilioNumberPrice>();
		Map<String, String> numberTypePricingMap = new HashMap<String, String>();
		String pricingUrl = TWILIO_PRICING_URL;
		pricingUrl = pricingUrl.replace("{Country}", countryIsoCode);
		GenericRestResponse twilioPriceResponse;
		twilioPriceResponse = ImiHttpUtil.defaultHttpGetHandler(pricingUrl,
				ImiBasicAuthUtil.getBasicAuthHash(provider));
		CountryPricing countryPricing = null;
		if (twilioPriceResponse.getResponseCode() == HttpStatus.OK.value()) {
			countryPricing = ImiJsonUtil.deserialize(twilioPriceResponse.getResponseBody(), CountryPricing.class);
		}
		// May need to remove seems redundant
		if (countryPricing != null) {
			for (InboundCallPrice inboundCallPrice : countryPricing.getInbound_call_prices()) {
				String basePrice = inboundCallPrice.getBase_price() == null ? inboundCallPrice.getCurrent_price()
						: inboundCallPrice.getBase_price();
				numberTypePricingMap.put(inboundCallPrice.getNumber_type().replace(" ", "").trim(), basePrice);
			}
		} else {
			LOG.error("Provider details seem to be improper. Please check account details" + provider.getName());
		}
		String line = "";
		String splitBy = ",";
		BufferedReader reader = null;
		twilioMonthlyPriceMap = new HashMap<String, TwilioNumberPrice>();
		try {
			InputStream in = getClass().getResourceAsStream(twilioPriceFilePath);
			if (in == null) {
				in = new FileInputStream(twilioPriceFilePath);
			}
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			int counter = 0;
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(splitBy);
				if (counter != 0 && row.length > 1) {
					String isoCode = row[0];
					String numberType = row[3];
					try {
						String twilioPricingMapKey = isoCode + "-" + (numberType.replace(" ", ""));
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
						twilioMonthlyPriceMap.put(twilioPricingMapKey.toUpperCase(), twilioNumberPrice);
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
		return twilioMonthlyPriceMap;
	}

	public Map<String, CountryPricing> getNexmoPricing(Provider provider) throws IOException {
		InputStream in = getClass().getResourceAsStream(nexmoPriceFilePath);
		if (in == null) {
			in = new FileInputStream(nexmoPriceFilePath);
		}
		HSSFWorkbook workbook = new HSSFWorkbook(in);
		HSSFSheet voicePriceSheet = workbook.getSheetAt(3);
		Map<String, CountryPricing> countryPricingMap = new HashMap<String, CountryPricing>();
		// voice pricing
		for (int i = 1; i < voicePriceSheet.getLastRowNum() + 1; i++) {
			CountryPricing countryPricing = new CountryPricing();
			Row row = voicePriceSheet.getRow(i);
			countryPricing.setCountry(row.getCell(0).toString().split(" - ")[1]);
			countryPricing.setCountryIsoCode(row.getCell(0).toString().split(" - ")[0]);
			Map<String, String> priceMap = new HashMap<String, String>();
			Map<String, Map<String, String>> pricing = new HashMap<String, Map<String, String>>();
			priceMap.put("monthlyRateInEuros", row.getCell(1).toString());
			priceMap.put("inboundRateInEuros", row.getCell(2).toString());
			pricing.put(LANDLINE, priceMap);
			priceMap = new HashMap<String, String>();
			priceMap.put("monthlyRateInEuros", row.getCell(3).toString());
			priceMap.put("inboundRateInEuros", row.getCell(4).toString());
			pricing.put(TOLLFREE, priceMap);
			countryPricing.setPricing(pricing);
			countryPricingMap.put(countryPricing.getCountryIsoCode(), countryPricing);
		}
		// sms pricing
		HSSFSheet smsPriceSheet = workbook.getSheetAt(2);
		for (int i = 1; i < smsPriceSheet.getLastRowNum() + 1; i++) {
			Row row = smsPriceSheet.getRow(i);
			String countryIso = row.getCell(0).toString().split(" - ")[0].trim();
			CountryPricing countryPricing = countryPricingMap.get(countryIso);
			if (countryPricing == null) {
				countryPricing = new CountryPricing();
				countryPricing.setCountry(row.getCell(0).toString().split(" - ")[1]);
				countryPricing.setCountryIsoCode(countryIso);
			}
			Map<String, String> priceMap = new HashMap<String, String>();
			Map<String, Map<String, String>> pricing = countryPricing.getPricing();
			priceMap.put("monthlyRateInEuros", row.getCell(1).toString());
			priceMap.put("inboundRateInEuros", row.getCell(2).toString());
			pricing.put(MOBILE, priceMap);
			countryPricing.setPricing(pricing);
			countryPricingMap.put(countryIso, countryPricing);
		}
		in.close();
		workbook.close();
		return countryPricingMap;
	}

}
