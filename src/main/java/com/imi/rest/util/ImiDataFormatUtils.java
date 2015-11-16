package com.imi.rest.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.imi.rest.constants.ServiceConstants;

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

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .format(new Date());
    }

    public static Byte getChannel(ServiceConstants serviceTypeEnum) {
        switch (serviceTypeEnum) {
        case VOICE:
            return 0;
        case SMS:
            return 1;
        case BOTH:
            return 2;
        }
        return null;
    }
    
    public static Date getmaxDate()
    {
        Calendar date = new GregorianCalendar(9999, 11, 31, 0, 0);
        return date.getTime();
    }

    public static String getAddressRestrictions(boolean addressRequired,
            String addressType) {
        String restrictions = "none";
        if (addressRequired) {
            restrictions = "Address is required to verify the purchase."
                    + addressType == null ? ""
                            : "Adress type required is " + addressType;
        }
        return restrictions;
    }

}
