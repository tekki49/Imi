package com.imi.rest.model;

public class NumberSearchResponse {
    private String number;
    private String region;
    private boolean smsEnabled;
    private boolean voiceEnabled;
    private String monthlyRental;
    private String smsRate;
    private String voiceRate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public boolean isVoiceEnabled() {
        return voiceEnabled;
    }

    public void setVoiceEnabled(boolean voiceEnabled) {
        this.voiceEnabled = voiceEnabled;
    }

    public String getMonthlyRental() {
        return monthlyRental;
    }

    public void setMonthlyRental(String monthlyRental) {
        this.monthlyRental = monthlyRental;
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
}
