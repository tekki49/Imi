package com.imi.rest.util;

public class DataFormatUtils {

    public static String forexConvert(Double double1, String input) {
        String monthlyRentalRateInGBP = null;
        try {
            double roundOff = Math.round(
                    Float.parseFloat(input) * double1 * 100.0) / 100.0;
            monthlyRentalRateInGBP = String.valueOf(roundOff);
        } catch (Exception e) {
        }
        return monthlyRentalRateInGBP;
    }

}
