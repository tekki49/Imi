package com.imi.rest.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationResponse {
    private String voiceMethod;
    private String voiceUrl;
    private String app_id;
    private String app_name;
    private Boolean default_app;
    private Boolean enabled;
    private String voiceFallback;
    private String voiceFallbackMethod;
    private String statusCallbackMethod;
    private String statusCallback;
    private String smsMethod;
    private String smsUrl;
    private Boolean public_uri;
    private String resource_uri;
    private String sip_uri;
    private String sub_account;
    private Boolean default_number_app;
    private Boolean default_endpoint_app;
    private String moHttpUrl;
    private String moSmppSysType;
    private String voiceCallbackType;
    private String voiceCallbackValue;
    private String smsFallbackUrl;
    private String smsFallbackMethod;
    private String smsApplicationSid;
    private String dateCreated;
    private String dateUpdated;
    private String voiceCallerIdLookup;
    private String voiceApplicationSid;
    private String trunkSid;
    private String accountSid;
    private String friendlyName;
    private String phoneNumber;
    private String apiVersion;
    private String uri;
    private String sid;
    private Map<String, Boolean> capabilities;
    private String api_id;
    private String error;
    private String address_requirements;

    public String getSmsFallbackUrl() {
        return smsFallbackUrl;
    }

    public String getSmsFallbackMethod() {
        return smsFallbackMethod;
    }

    public String getSmsApplicationSid() {
        return smsApplicationSid;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public String getVoiceCallerIdLookup() {
        return voiceCallerIdLookup;
    }

    public String getVoiceApplicationSid() {
        return voiceApplicationSid;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getUri() {
        return uri;
    }

    @JsonIgnore
    public Map<String, Boolean> getCapabilities() {
        return capabilities;
    }

    public String getMoHttpUrl() {
        return moHttpUrl;
    }

    public void setMoHttpUrl(String moHttpUrl) {
        this.moHttpUrl = moHttpUrl;
    }

    public String getMoSmppSysType() {
        return moSmppSysType;
    }

    public void setMoSmppSysType(String moSmppSysType) {
        this.moSmppSysType = moSmppSysType;
    }

    public String getVoiceCallbackType() {
        return voiceCallbackType;
    }

    public void setVoiceCallbackType(String voiceCallbackType) {
        this.voiceCallbackType = voiceCallbackType;
    }

    public String getVoiceCallbackValue() {
        return voiceCallbackValue;
    }

    public void setVoiceCallbackValue(String voiceCallbackValue) {
        this.voiceCallbackValue = voiceCallbackValue;
    }

    @JsonProperty("answer_method")
    public void setAnswer_method(String voiceMethod) {
        this.voiceMethod = voiceMethod;
    }

    @JsonProperty("voice_method")
    public void setVoice_method(String voiceMethod) {
        this.voiceMethod = voiceMethod;
    }

    @JsonProperty("answer_url")
    public void setAnswer_url(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    @JsonProperty("voice_url")
    public void setVoice_url(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    @JsonProperty("sms_fallback_url")
    public void setSms_fallback_url(String smsFallbackUrl) {
        this.smsFallbackUrl = smsFallbackUrl;
    }

    @JsonProperty("sms_fallback_method")
    public void setSms_fallback_method(String smsFallbackMethod) {
        this.smsFallbackMethod = smsFallbackMethod;
    }

    @JsonProperty("sms_application_sid")
    public void setSms_application_sid(String smsApplicationSid) {
        this.smsApplicationSid = smsApplicationSid;
    }

    @JsonProperty("date_created")
    public void setDate_created(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty("date_updated")
    public void setDate_updated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @JsonProperty("voice_caller_id_lookup")
    public void setVoice_caller_id_lookup(String voiceCallerIdLookup) {
        this.voiceCallerIdLookup = voiceCallerIdLookup;
    }

    @JsonProperty("voice_application_sid")
    public void setVoice_application_sid(String voiceApplicationSid) {
        this.voiceApplicationSid = voiceApplicationSid;
    }

    @JsonProperty("account_sid")
    public void setAccount_sid(String accountSid) {
        this.accountSid = accountSid;
    }

    @JsonProperty("friendly_name")
    public void setFriendly_name(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @JsonProperty("phone_number")
    public void setPhone_number(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("api_version")
    public void setApi_version(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonProperty("capabilities")
    public void setCapabilities(Map<String, Boolean> capabilities) {
        this.capabilities = capabilities;
    }

    @JsonProperty("fallback_answer_url")
    public void setFallback_answer_url(String voiceFallback) {
        this.voiceFallback = voiceFallback;
    }

    @JsonProperty("voice_fallback_url")
    public void setVoice_fallback_url(String voiceFallback) {
        this.voiceFallback = voiceFallback;
    }

    @JsonProperty("fallback_method")
    public void setFallback_method(String voiceFallbackMethod) {
        this.voiceFallbackMethod = voiceFallbackMethod;
    }

    @JsonProperty("voice_fallback_method")
    public void setVoice_fallback_method(String voiceFallbackMethod) {
        this.voiceFallbackMethod = voiceFallbackMethod;
    }

    @JsonProperty("hangup_method")
    public void setHangup_method(String statusCallbackMethod) {
        this.statusCallbackMethod = statusCallbackMethod;
    }

    @JsonProperty("status_callback_method")
    public void setStatus_callback_method(String statusCallbackMethod) {
        this.statusCallbackMethod = statusCallbackMethod;
    }

    @JsonProperty("hangup_url")
    public void setHangup_url(String statusCallback) {
        this.statusCallback = statusCallback;
    }

    @JsonProperty("status_callback")
    public void setStatus_callback(String statusCallback) {
        this.statusCallback = statusCallback;
    }

    @JsonProperty("voiceStatusCallback")
    public void setVoiceStatusCallback(String statusCallback) {
        this.statusCallback = statusCallback;
    }

    @JsonProperty("message_method")
    public void setMessage_method(String smsMethod) {
        this.smsMethod = smsMethod;
    }

    @JsonProperty("sms_method")
    public void SetSms_method(String smsMethod) {
        this.smsMethod = smsMethod;
    }

    @JsonProperty("message_url")
    public void setMessage_url(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    @JsonProperty("sms_url")
    public void setSms_url(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    @JsonProperty("sid")
    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getApp_id() {
        return app_id;
    }

    @JsonProperty("app_id")
    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    @JsonProperty("app_name")
    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public Boolean getDefault_app() {
        return default_app;
    }

    @JsonProperty("default_app")
    public void setDefault_app(Boolean default_app) {
        this.default_app = default_app;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getVoiceMethod() {
        return voiceMethod;
    }

    @JsonProperty("voiceMethod")
    public void setVoiceMethod(String voiceMethod) {
        this.voiceMethod = voiceMethod;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    @JsonProperty("voiceUrl")
    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getVoiceFallback() {
        return voiceFallback;
    }

    @JsonProperty("voiceFallback")
    public void setVoiceFallback(String voiceFallback) {
        this.voiceFallback = voiceFallback;
    }

    public String getVoiceFallbackMethod() {
        return voiceFallbackMethod;
    }

    @JsonProperty("voiceFallbackMethod")
    public void setVoiceFallbackMethod(String voiceFallbackMethod) {
        this.voiceFallbackMethod = voiceFallbackMethod;
    }

    public String getStatusCallbackMethod() {
        return statusCallbackMethod;
    }

    @JsonProperty("statusCallbackMethod")
    public void setStatusCallbackMethod(String statusCallbackMethod) {
        this.statusCallbackMethod = statusCallbackMethod;
    }

    public String getStatusCallback() {
        return statusCallback;
    }

    @JsonProperty("statusCallback")
    public void setStatusCallback(String statusCallback) {
        this.statusCallback = statusCallback;
    }

    public String getSmsMethod() {
        return smsMethod;
    }

    @JsonProperty("smsMethod")
    public void setSmsMethod(String smsMethod) {
        this.smsMethod = smsMethod;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    @JsonProperty("smsUrl")
    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public Boolean getPublic_uri() {
        return public_uri;
    }

    @JsonProperty("public_uri")
    public void setPublic_uri(Boolean public_uri) {
        this.public_uri = public_uri;
    }

    public String getResource_uri() {
        return resource_uri;
    }

    @JsonProperty("resource_uri")
    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    public String getSip_uri() {
        return sip_uri;
    }

    @JsonProperty("sip_uri")
    public void setSip_uri(String sip_uri) {
        this.sip_uri = sip_uri;
    }

    public String getSub_account() {
        return sub_account;
    }

    @JsonProperty("sub_account")
    public void setSub_account(String sub_account) {
        this.sub_account = sub_account;
    }

    public Boolean getDefault_number_app() {
        return default_number_app;
    }

    @JsonProperty("default_number_app")
    public void setDefault_number_app(Boolean default_number_app) {
        this.default_number_app = default_number_app;
    }

    public Boolean getDefault_endpoint_app() {
        return default_endpoint_app;
    }

    @JsonProperty("default_endpoint_app")
    public void setDefault_endpoint_app(Boolean default_endpoint_app) {
        this.default_endpoint_app = default_endpoint_app;
    }

    public String getSid() {
        return sid;
    }

    public String getTrunkSid() {
        return trunkSid;
    }

    @JsonProperty("trunkSid")
    public void setTrunkSid(String trunkSid) {
        this.trunkSid = trunkSid;
    }

    @JsonIgnore
    public String getApi_id() {
        return api_id;
    }

    @JsonProperty("api_id")
    public void setApi_id(String api_id) {
        this.api_id = api_id;
    }

    @JsonIgnore
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(String error) {
        this.error = error;
    }

    @JsonIgnore
    public String getAddress_requirements() {
        return address_requirements;
    }

    @JsonProperty("address_requirements")
    public void setAddress_requirements(String address_requirements) {
        this.address_requirements = address_requirements;
    }

    public void setSmsFallbackUrl(String smsFallbackUrl) {
        this.smsFallbackUrl = smsFallbackUrl;
    }

    public void setSmsFallbackMethod(String smsFallbackMethod) {
        this.smsFallbackMethod = smsFallbackMethod;
    }

    public void setSmsApplicationSid(String smsApplicationSid) {
        this.smsApplicationSid = smsApplicationSid;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setVoiceCallerIdLookup(String voiceCallerIdLookup) {
        this.voiceCallerIdLookup = voiceCallerIdLookup;
    }

    public void setVoiceApplicationSid(String voiceApplicationSid) {
        this.voiceApplicationSid = voiceApplicationSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

}
