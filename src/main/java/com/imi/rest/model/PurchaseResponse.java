package com.imi.rest.model;

import com.imi.rest.dao.model.Purchase;

public class PurchaseResponse {

    private Integer id;
    private String number;
    private String numberType;
    private String restrictions;
    private String monthlyRentalRate;
    private String setUpRate;
    private String smsRate;
    private String voicePrice;
    private String effectiveDate;
    private Integer resourceManagerId;
    private Integer countryProviderId;
    private String status;
    private boolean addressRequired;
    // for Twilio
    private String numberSid;
    private String accountSid;

    public PurchaseResponse() {

    }

    public PurchaseResponse(Purchase purchase) {
        this.id = purchase.getId();
        this.number = purchase.getNumber().toString();
        this.numberType = purchase.getNumberType();
        this.restrictions = purchase.getRestrictions();
        this.monthlyRentalRate = purchase.getMonthlyRentalRate();
        this.setUpRate = purchase.getSetUpRate();
        this.smsRate = purchase.getSmsRate();
        this.voicePrice = purchase.getVoicePrice();
        this.effectiveDate = purchase.getEffectiveDate();
        this.resourceManagerId = purchase.getResouceManagerId();
        this.countryProviderId = purchase.getProvidercountry().getId();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
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

    public String getVoicePrice() {
        return voicePrice;
    }

    public void setVoicePrice(String voicePrice) {
        this.voicePrice = voicePrice;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getResourceManagerId() {
        return resourceManagerId;
    }

    public void setResourceManagerId(Integer resourceManagerId) {
        this.resourceManagerId = resourceManagerId;
    }

    public Integer getCountryProviderId() {
        return countryProviderId;
    }

    public void setCountryProviderId(Integer countryProviderId) {
        this.countryProviderId = countryProviderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAddressRequired() {
        return addressRequired;
    }

    public void setAddressRequired(boolean addressRequired) {
        this.addressRequired = addressRequired;
    }

    public String getNumberSid() {
        return numberSid;
    }

    public void setNumberSid(String numberSid) {
        this.numberSid = numberSid;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

}
