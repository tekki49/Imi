package com.imi.rest.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Number {

	private String number;
	private String numberType;
	private String serviceType;
	private String country;
	private String monthlyRentalRate;
	private String setUpRate;
	private String smsRate;
	private String voiceRate;
	private String provider;
	private String restriction;
	private String lata;
	private String prefix;
	private String rate_center;
	private String region;
	private String resource_uri;
	private String restriction_text;
	private boolean smsEnabled;
	private boolean voiceEnabled;
	private Map<String, Boolean> capabilities;
	private List<String> features;
	private String priceUnit;
	private String added_on;
	private String alias;
	private String api_id;
	private String application;
	private String sub_account;
	private String status;
	private String error;

	public String getNumber() {
		return number;
	}

	@JsonProperty("number")
	public void setNumber(String number) {
		this.number = number;
	}

	@JsonProperty("msisdn")
	public void setMsisdn(String number) {
		this.number = number;
	}

	@JsonProperty("phone_number")
	public void setPhoneNumber(String number) {
		this.number = number;
	}

	public String getNumberType() {
		return numberType;
	}

	@JsonProperty("numberType")
	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	@JsonProperty("number_type")
	public void setnumber_type(String numberType) {
		this.numberType = numberType;
	}

	@JsonProperty("type")
	public void setType(String numberType) {
		this.numberType = numberType;
	}

	public String getServiceType() {
		return serviceType;
	}

	@JsonProperty("serviceType")
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCountry() {
		return country;
	}

	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

	public String getMonthlyRentalRate() {
		return monthlyRentalRate;
	}

	@JsonProperty("monthlyRentalRate")
	public void setMonthlyRentalRate(String monthlyRentalRate) {
		this.monthlyRentalRate = monthlyRentalRate;
	}

	@JsonProperty("monthly_rental_rate")
	public void setmonthly_rental_rate(String monthlyRentalRate) {
		this.monthlyRentalRate = monthlyRentalRate;
	}

	@JsonIgnore
	public String getSetUpRate() {
		return setUpRate;
	}

	@JsonProperty("setUpRate")
	public void setSetUpRate(String setUpRate) {
		this.setUpRate = setUpRate;
	}

	@JsonProperty("setup_rate")
	public void setsetup_rate(String setUpRate) {
		this.setUpRate = setUpRate;
	}

	public String getSmsRate() {
		return smsRate;
	}

	@JsonProperty("smsRate")
	public void setSmsRate(String smsRate) {
		this.smsRate = smsRate;
	}

	@JsonProperty("sms_rate")
	public void setsms_rate(String smsRate) {
		this.smsRate = smsRate;
	}

	public String getVoiceRate() {
		return voiceRate;
	}

	@JsonProperty("voiceRate")
	public void setVoiceRate(String voiceRate) {
		this.voiceRate = voiceRate;
	}

	@JsonProperty("voice_rate")
	public void setvoice_rate(String voiceRate) {
		this.voiceRate = voiceRate;
	}

	@JsonIgnore
	public String getAdded_on() {
		return added_on;
	}

	public void setAdded_on(String added_on) {
		this.added_on = added_on;
	}

	@JsonIgnore
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@JsonIgnore
	public String getApi_id() {
		return api_id;
	}

	public void setApi_id(String api_id) {
		this.api_id = api_id;
	}

	@JsonIgnore
	public String getApplication() {
		return application;
	}

	@JsonProperty("application")
	public void setApplication(String application) {
		this.application = application;
	}

	@JsonIgnore
	public String getSub_account() {
		return sub_account;
	}

	public void setSub_account(String sub_account) {
		this.sub_account = sub_account;
	}

	public String getProvider() {
		return provider;
	}

	@JsonProperty("provider")
	public void setProvider(String provider) {
		this.provider = provider;
	}

	@JsonProperty("carrier")
	public void setCarrier(String provider) {
		this.provider = provider;
	}

	@JsonIgnore
	public String getRestriction() {
		return restriction;
	}

	@JsonProperty("restriction")
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	@JsonIgnore
	public String getLata() {
		return lata;
	}

	@JsonProperty("lata")
	public void setLata(String lata) {
		this.lata = lata;
	}

	@JsonIgnore
	public String getPrefix() {
		return prefix;
	}

	@JsonProperty("prefix")
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@JsonIgnore
	public String getRate_center() {
		return rate_center;
	}

	@JsonProperty("rate_center")
	public void setRate_center(String rate_center) {
		this.rate_center = rate_center;
	}

	public String getRegion() {
		return region;
	}

	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
	}

	@JsonIgnore
	public String getResource_uri() {
		return resource_uri;
	}

	@JsonProperty("resource_uri")
	public void setResource_uri(String resource_uri) {
		this.resource_uri = resource_uri;
	}

	@JsonIgnore
	public String getRestriction_text() {
		return restriction_text;
	}

	@JsonProperty("restriction_text")
	public void setRestriction_text(String restriction_text) {
		this.restriction_text = restriction_text;
	}

	public boolean isSmsEnabled() {
		return smsEnabled;
	}

	@JsonProperty("sms_enabled")
	public void setSmsEnabled(boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

	public boolean isVoiceEnabled() {
		return voiceEnabled;
	}

	@JsonProperty("voice_enabled")
	public void setVoiceEnabled(boolean voiceEnabled) {
		this.voiceEnabled = voiceEnabled;
	}

	@JsonIgnore
	public Map<String, Boolean> getCapabilities() {
		return capabilities;
	}

	@JsonProperty("capabilities")
	public void setCapabilities(Map<String, Boolean> capabilities) {
		this.capabilities = capabilities;
	}

	@JsonIgnore
	public List<String> getFeatures() {
		return features;
	}

	@JsonProperty("features")
	public void setFeatures(List<String> features) {
		this.features = features;
	}

	@JsonIgnore
	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
	public String getError() {
		return error;
	}

	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
	}

}
