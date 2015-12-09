package com.imi.rest.model;

import com.imi.rest.dao.model.Purchase;

public class PurchaseResponse {
    private String number;
    private String numberType;
    private String provider;
    private String countryIso;
    private String services;
    private boolean addressRequired;
    private String monthlyRentalRate;
    private String setUpRate;
    private String smsRate;
    private String voicePrice;
    private String effectiveDate;
    private String status;

    public PurchaseResponse() {
    }

    public PurchaseResponse(Purchase purchase) {
        this.number = "" + purchase.getNumber();
        this.provider = purchase.getNumberProviderCountry() == null ? null
                : purchase.getNumberProviderCountry().getNumberProvider()
                        .getName();
        this.countryIso = purchase.getNumberProviderCountry()
                .getResourceCountry().getCountryIso();
        this.monthlyRentalRate = purchase.getMonthlyRentalRate();
        this.setUpRate = purchase.getSetUpRate();
        this.smsRate = purchase.getSmsRate();
        this.voicePrice = purchase.getVoicePrice();
        // TODO status
        // this.status=purchase.getStatus();
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

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

}
