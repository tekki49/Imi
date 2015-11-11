package com.imi.rest.util;

public class ImiDataFormatUtils {

    public static String forexConvert(Double forex, String input) {
        String monthlyRentalRateInGBP = null;
        try {
            double roundOff = Math
                    .round(Float.parseFloat(input) * forex * 100.00) / 100.00;
            if (roundOff == 0) {
                roundOff = Math.round(Float.parseFloat(input) * forex * 1000.00)
                        / 1000.00;
            }
            if (roundOff == 0) {
                roundOff = Math.round(
                        Float.parseFloat(input) * forex * 10000.00) / 10000.00;
            }
            if (roundOff == 0) {
                roundOff = Math
                        .round(Float.parseFloat(input) * forex * 100000.00)
                        / 100000.00;
            }
            monthlyRentalRateInGBP = String.valueOf(roundOff);
        } catch (Exception e) {
        }
        return monthlyRentalRateInGBP;
    }

}
