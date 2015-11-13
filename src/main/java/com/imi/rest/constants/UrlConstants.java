package com.imi.rest.constants;

public interface UrlConstants {
    // Plivo Constants
    public static final String PLIVO_PHONE_SEARCH_URL = "https://api.plivo.com/v1/Account/{auth_id}/PhoneNumber?country_iso={country_iso}&type={type}&services={services}&pattern={pattern}";
    public static final String PLIVO_PURCHASE_URL = "https://api.plivo.com/v1/Account/{auth_id}/PhoneNumber/{number}//";
    public static final String PLIVO_RELEASE_URL = "https://api.plivo.com/v1/Account/{auth_id}/Number/{number}/";
    public static final String PLIVO_ALL_NUMBERS_URL = "https://api.plivo.com/v1/Account/{auth_id}/Number/";
    public static final String PLIVO_ACCOUNT_BALANCE_URL = "https://api.plivo.com/v1/Account/{auth_id}/";
    public static final String PLIVO_APPLICATION_CREATE_URL = "https://api.plivo.com/v1/Account/{auth_id}/Application/";
    public static final String PLIVO_APPLICATION_UPDATE_URL = "https://api.plivo.com/v1/Account/{auth_id}/Application/{app_id}/";

    // Twilio Constants
    public static final String TWILIO_PHONE_SEARCH_URL = "https://api.twilio.com/2010-04-01/Accounts/{auth_id}/AvailablePhoneNumbers/{country_iso}/{type}.json?Contains={pattern}&{services}";
    public static final String TWILIO_COUNTRY_LIST_URL = "https://pricing.twilio.com/v1/PhoneNumbers/Countries";
    public static final String TWILIO_ALL_NUMBER_PURCHASED_URL = "https://api.twilio.com/2010-04-01/Accounts/{auth_id}/IncomingPhoneNumbers.json";
    public static final String TWILIO_PURCHASE_URL = "https://api.twilio.com/2010-04-01/Accounts/{auth_id}/IncomingPhoneNumbers.json?PhoneNumber={number}";
    public static final String TWILIO_RELEASE_URL = "https://api.twilio.com/2010-04-01/Accounts/{auth_id}/IncomingPhoneNumbers/{IncomingPhoneNumberSid}.json";
    public static final String TWILIO_ACCOUNT_URL = "https://api.twilio.com/2010-04-01/Accounts.json";
    public static final String TWILIO_PRICING_URL = "https://pricing.twilio.com/v1/Voice/Countries/{Country}";
    public static final String TWILIO_DUMMY_PURCHASE_URL = "https://api.twilio.com/2010-04-01/Accounts/{auth_id}/IncomingPhoneNumbers.json";
    public static final String TWILIO_NUMBER_UPDATE_URL = "https://api.twilio.com/2010-04-01/Accounts/{auth_id}/IncomingPhoneNumbers/{IncomingPhoneNumberSid}.json";

    // Nexmo Constants
    public static final String NEXMO_PHONE_SEARCH_URL = "https://rest.nexmo.com/number/search/{api_key}/{api_secret}/{country_iso}?pattern={pattern}&size=100&features={features}&&search_pattern=1&&index={index}";
    public static final String NEXMO_PURCHASE_URL = "https://rest.nexmo.com/number/buy?api_key={api_key}&api_secret={api_secret}&country={country}&msisdn={msisdn}";
    public static final String NEXMO_RELEASE_URL = "https://rest.nexmo.com/number/cancel/{api_key}/{api_secret}/{country}/{msisdn}";
    public static final String NEXMO_ACCOUNT_BALANCE_URL = "https://rest.nexmo.com/account/get-balance/{api_key}/{api_secret}";
    public static final String NEXMO_NUMBER_UPDATE_URL = "https://rest.nexmo.com/number/update/{api_key}/{api_secret}/{country}/{msisdn}";

}
