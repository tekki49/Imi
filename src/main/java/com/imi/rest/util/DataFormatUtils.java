package com.imi.rest.util;

public class DataFormatUtils {

    public static String forexConvert(float forexConversion, String input) {
        String monthlyRentalRateInGBP = null;
        try {
            double roundOff = Math.round(
                    Float.parseFloat(input) * forexConversion * 100.0) / 100.0;
            monthlyRentalRateInGBP = String.valueOf(roundOff);
        } catch (Exception e) {
        }
        return monthlyRentalRateInGBP;
    }

}
