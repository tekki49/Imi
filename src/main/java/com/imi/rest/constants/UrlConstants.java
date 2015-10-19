package com.imi.rest.constants;

public interface UrlConstants {

    public static final String PLIVIO_PHONE_SEARCH_URL = "https://api.plivo.com/v1/Account/MANMMWNGMWMZNKNDIWOD/PhoneNumber?country_iso={country_iso}&type={type}&services={services}&pattern={pattern}";
    public static final String TWILIO_PHONE_SEARCH_URL = "https://api.twilio.com/2010-04-01/Accounts/AC606f86ee4172ff7773d4162e7b62496c/AvailablePhoneNumbers/{country_iso}/Local.json?Contains={pattern}&{services}";
    public static final String NEXMO_PHONE_SEARCH_URL = "https://rest.nexmo.com/number/search/{api_key}/{api_secret}/{country_iso}?pattern={pattern}&size=100&features={features}&&search_pattern=1&&index={index}";
    public static final String TWILIO_COUNTRY_LIST_URL = "https://pricing.twilio.com/v1/PhoneNumbers/Countries";
}
