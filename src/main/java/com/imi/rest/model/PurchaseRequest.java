package com.imi.rest.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String number;
    private String numberType;
    private String service;
    private String countryIso;
    private String provider;
    private String monthlyRentalRate;
    private String voiceRate;
    private String smsRate;

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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMonthlyRentalRate() {
        return monthlyRentalRate;
    }

    public void setMonthlyRentalRate(String monthlyRentalRate) {
        this.monthlyRentalRate = monthlyRentalRate;
    }

    public String getVoiceRate() {
        return voiceRate;
    }

    public void setVoiceRate(String voiceRate) {
        this.voiceRate = voiceRate;
    }

    public String getSmsRate() {
        return smsRate;
    }

    public void setSmsRate(String smsRate) {
        this.smsRate = smsRate;
    }
}
