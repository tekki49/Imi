package com.imi.rest.model;

public class TwilioNumberPrice {

	private String voiceEnabled;
	private String trunkingEnabled;
	private String smsEnabled;
	private String mmsEnabled;
	private String domesticVoiceOnly;
	private String domesticSmsOnly;
	private String monthlyRentalRate;
	private String inboundVoicePrice;
	private String inboundTrunkingPrice;
	private String inboundSmsPrice;
	private String inboundMmsPrice;
	private String BetaStatus;
	private String addressRequired;

	public String getVoiceEnabled() {
		return voiceEnabled;
	}

	public void setVoiceEnabled(String voiceEnabled) {
		this.voiceEnabled = voiceEnabled;
	}

	public String getTrunkingEnabled() {
		return trunkingEnabled;
	}

	public void setTrunkingEnabled(String trunkingEnabled) {
		this.trunkingEnabled = trunkingEnabled;
	}

	public String getSmsEnabled() {
		return smsEnabled;
	}

	public void setSmsEnabled(String smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

	public String getMmsEnabled() {
		return mmsEnabled;
	}

	public void setMmsEnabled(String mmsEnabled) {
		this.mmsEnabled = mmsEnabled;
	}

	public String getDomesticVoiceOnly() {
		return domesticVoiceOnly;
	}

	public void setDomesticVoiceOnly(String domesticVoiceOnly) {
		this.domesticVoiceOnly = domesticVoiceOnly;
	}

	public String getDomesticSmsOnly() {
		return domesticSmsOnly;
	}

	public void setDomesticSmsOnly(String domesticSmsOnly) {
		this.domesticSmsOnly = domesticSmsOnly;
	}

	public String getMonthlyRentalRate() {
		return monthlyRentalRate;
	}

	public void setMonthlyRentalRate(String monthlyRentalRate) {
		this.monthlyRentalRate = monthlyRentalRate;
	}

	public String getInboundVoicePrice() {
		return inboundVoicePrice;
	}

	public void setInboundVoicePrice(String inboundVoicePrice) {
		this.inboundVoicePrice = inboundVoicePrice;
	}

	public String getInboundTrunkingPrice() {
		return inboundTrunkingPrice;
	}

	public void setInboundTrunkingPrice(String inboundTrunkingPrice) {
		this.inboundTrunkingPrice = inboundTrunkingPrice;
	}

	public String getInboundSmsPrice() {
		return inboundSmsPrice;
	}

	public void setInboundSmsPrice(String inboundSmsPrice) {
		this.inboundSmsPrice = inboundSmsPrice;
	}

	public String getInboundMmsPrice() {
		return inboundMmsPrice;
	}

	public void setInboundMmsPrice(String inboundMmsPrice) {
		this.inboundMmsPrice = inboundMmsPrice;
	}

	public String getBetaStatus() {
		return BetaStatus;
	}

	public void setBetaStatus(String betaStatus) {
		BetaStatus = betaStatus;
	}

	public String getAddressRequired() {
		return addressRequired;
	}

	public void setAddressRequired(String addressRequired) {
		this.addressRequired = addressRequired;
	}
}
