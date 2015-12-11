package com.imi.rest.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import com.imi.rest.constants.ServiceConstants;
import com.imi.rest.model.PurchaseRequest;

public class ImiDataFormatUtils {

	public static String forexConvert(Double forex, String input) {
		String monthlyRentalRateInGBP = null;
		try {
			/*
			 * double roundOff = Math.round(Float.parseFloat(input) * forex
			 * 100.00) / 100.00; if (roundOff == 0) { roundOff = Math
			 * .round(Float.parseFloat(input) * forex * 1000.00) / 1000.00; } if
			 * (roundOff == 0) { roundOff = Math.round(Float.parseFloat(input) *
			 * forex 10000.00) / 10000.00; } if (roundOff == 0) { roundOff =
			 * Math.round(Float.parseFloat(input) * forex 100000.00) /
			 * 100000.00; }
			 */
			monthlyRentalRateInGBP = String.valueOf(Float.parseFloat(input) * forex);
		} catch (Exception e) {
		}
		return monthlyRentalRateInGBP;
	}

	public static String getCurrentTimeStamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}

	public static String fromDateToTimeStamp(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
	}

	public static Byte getChannel(ServiceConstants serviceTypeEnum) {
		switch (serviceTypeEnum) {
		case VOICE:
			return 2;
		case SMS:
			return 0;
		case BOTH:
			return 3;
		}
		return null;
	}

	public static Date getmaxDate() {
		Calendar date = new GregorianCalendar(9999, 11, 31, 0, 0);
		return date.getTime();
	}

	public static String getmaxDateString() {
		Calendar date = new GregorianCalendar(9999, 11, 31, 0, 0);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date.getTime());
	}

	public static String getAddressRestrictions(boolean addressRequired, String addressType) {
		String restrictions = "none";
		if (addressRequired) {
			restrictions = "Address is required to verify the purchase." + addressType == null ? ""
					: "Adress type required is " + addressType;
		}
		return restrictions;
	}

	public static boolean isNumber(String value) {
		boolean isNumber = false;
		try {
			Float.parseFloat(value);
			isNumber = true;
		} catch (Exception e) {
		}
		return isNumber;
	}

	public static final String getHikedPrice(String currentPrice, String hikePercentage) {
		String hikedPrice = null;
		if (isNumber(currentPrice)) {
			hikedPrice = currentPrice;
			if (isNumber(hikePercentage)) {
				/*
				 * double roundOff = Math.round(Float.parseFloat(currentPrice)
				 * (100 + Float.parseFloat(hikePercentage))) / 100.00; if
				 * (roundOff == 0) { roundOff =
				 * Math.round(Float.parseFloat(currentPrice) (100 +
				 * Float.parseFloat(hikePercentage)) * 10) / 1000.00; } if
				 * (roundOff == 0) { roundOff =
				 * Math.round(Float.parseFloat(currentPrice) (100 +
				 * Float.parseFloat(hikePercentage)) * 100) / 10000.00; } if
				 * (roundOff == 0) { roundOff =
				 * Math.round(Float.parseFloat(currentPrice) (100 +
				 * Float.parseFloat(hikePercentage)) * 1000) / 100000.00; }
				 */
				hikedPrice = String
						.valueOf(Float.parseFloat(currentPrice) * (100 + Float.parseFloat(hikePercentage)) / 100);
			}
		}
		return hikedPrice;
	}

	public static PurchaseRequest formatPurchaseRequest(PurchaseRequest purchaseRequest) {
		if (purchaseRequest != null) {
			if (purchaseRequest.getMonthlyRentalRate() != null) {
				purchaseRequest.setMonthlyRentalRate(purchaseRequest.getMonthlyRentalRate().toUpperCase()
						.replace("GBP", "").replace("N/A", "").replace("FREE", ""));
			}
			if (purchaseRequest.getVoiceRate() != null) {
				purchaseRequest.setVoiceRate(purchaseRequest.getVoiceRate().toUpperCase().replace("GBP", "")
						.replace("N/A", "").replace("FREE", ""));
			}
			if (purchaseRequest.getSmsRate() != null) {
				purchaseRequest.setSmsRate(purchaseRequest.getSmsRate().toUpperCase().replace("GBP", "")
						.replace("N/A", "").replace("FREE", ""));
			}
			if (purchaseRequest.getNumber() != null) {
				purchaseRequest.setNumber(purchaseRequest.getNumber().replace("+", ""));
			}
		}
		return purchaseRequest;
	}

	public static String roundOffToN(String input, int n) {
		try {
			BigDecimal bigDecimal = new BigDecimal(input);
			bigDecimal = bigDecimal.setScale(n, BigDecimal.ROUND_HALF_UP);
			if (n == 0) {
				input = String.valueOf(bigDecimal.intValue());
			} else {
				input = String.valueOf(bigDecimal.doubleValue());
			}
		} catch (Exception e) {
		}
		return input;
	}

	public static String multiply(String input, String multiplicationFactor) {
		try {
			input = String.valueOf(Float.parseFloat(input) * Float.parseFloat(multiplicationFactor));
		} catch (Exception e) {
		}
		return input;
	}

	public static String getCurrentDate(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	public static long getDifferenceInDaysBetweenDates(Date startDate, Date endDate) {
		long diff = endDate.getTime() - startDate.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static Date getDateFromString(String dateString, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
		}
		return date;
	}

	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
}
