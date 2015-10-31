package com.imi.rest.constants;

public interface UrlConstants {
    // Plivio Constants
    public static final String PLIVO_PHONE_SEARCH_URL = "https://api.plivo.com/v1/Account/MANMMWNGMWMZNKNDIWOD/PhoneNumber?country_iso={country_iso}&type={type}&services={services}&pattern={pattern}";
    public static final String PLIVO_PURCHASE_URL = "https://api.plivo.com/v1/Account/MANMMWNGMWMZNKNDIWOD/PhoneNumber/{number}/";
    public static final String PLIVO_RELEASE_URL = "https://api.plivo.com/v1/Account/MANMMWNGMWMZNKNDIWOD/Number/{number}/";

    // Twilio Constants
    public static final String TWILIO_PHONE_SEARCH_URL = "https://api.twilio.com/2010-04-01/Accounts/AC606f86ee4172ff7773d4162e7b62496c/AvailablePhoneNumbers/{country_iso}/Local.json?Contains={pattern}&{services}";
    public static final String TWILIO_COUNTRY_LIST_URL = "https://pricing.twilio.com/v1/PhoneNumbers/Countries";
    public static final String TWILIO_PURCHASE_URL = "https://api.twilio.com/2010-04-01/Accounts/AC606f86ee4172ff7773d4162e7b62496c/IncomingPhoneNumbers?PhoneNumber={number}";
    public static final String TWILIO_RELEASE_URL = "https://api.twilio.com/2010-04-01/Accounts/AC606f86ee4172ff7773d4162e7b62496c/IncomingPhoneNumbers?PhoneNumber={number}";
    public static final String TWILIO_PRICING_URL = "https://pricing.twilio.com/v1/Voice/Countries/{Country}";
    public static final String TWILIO_DUMMY_PURCHASE_URL = "https://api.twilio.com/2010-04-01/Accounts/AC0503dc5f717b9938dea12a88a865bf9f/IncomingPhoneNumbers?PhoneNumber={number}";

    // Nexmo Constants
    public static final String NEXMO_PHONE_SEARCH_URL = "https://rest.nexmo.com/number/search/{api_key}/{api_secret}/{country_iso}?pattern={pattern}&size=100&features={features}&&search_pattern=1&&index={index}";
    public static final String NEXMO_PURCHASE_URL = "https://rest.nexmo.com/number/search/{api_key}/{api_secret}/{country}/{msisdn}";
    public static final String NEXMO_RELEASE_URL = "https://rest.nexmo.com/number/search/{api_key}/{api_secret}/{country}/{msisdn}";

}
