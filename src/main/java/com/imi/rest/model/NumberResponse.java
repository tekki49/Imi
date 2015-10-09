package com.imi.rest.model;

public class NumberResponse {

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMonthlyRentalRate() {
		return monthlyRentalRate;
	}

	public void setMonthlyRentalRate(String monthlyRentalRate) {
		this.monthlyRentalRate = monthlyRentalRate;
	}

	public String getSetUpRate() {
		return setUpRate;
	}

	public void setSetUpRate(String setUpRate) {
		this.setUpRate = setUpRate;
	}

	public String getSmsRate() {
		return smsRate;
	}

	public void setSmsRate(String smsRate) {
		this.smsRate = smsRate;
	}

	public String getVoiceRate() {
		return voiceRate;
	}

	public void setVoiceRate(String voiceRate) {
		this.voiceRate = voiceRate;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getRestriction() {
		return restriction;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

}
