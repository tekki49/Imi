package com.imi.rest.dao.model;

// Generated 25 Nov, 2015 2:51:32 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * NumberPurchase generated by hbm2java
 */
@Entity
@Table(name = "purchase", schema = "ump_resource_mgr")
public class Purchase implements java.io.Serializable {

    private Integer id;
    private Providercountry numberProviderCountry;
    private String number;
    private String numberType;
    private String restrictions;
    private String monthlyRentalRate;
    private String setUpRate;
    private String smsRate;
    private String voicePrice;
    private String effectiveDate;
    private Integer resouceManagerId;
    // saving numbersid and subaccountsid will be better handling the twilio
    // case
    private String numbersid;
    private String subaccountsid;
    private String clientId;

    public Purchase() {
    }

    public Purchase(Providercountry numberProviderCountry, String number,
            String numberType, String restrictions, String monthlyRentalRate,
            String setUpRate, String smsRate, String voicePrice,
            String effectiveDate, Integer resouceManagerId) {
        this.numberProviderCountry = numberProviderCountry;
        this.number = number;
        this.numberType = numberType;
        this.restrictions = restrictions;
        this.monthlyRentalRate = monthlyRentalRate;
        this.setUpRate = setUpRate;
        this.smsRate = smsRate;
        this.voicePrice = voicePrice;
        this.effectiveDate = effectiveDate;
        this.resouceManagerId = resouceManagerId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CountryProviderId")
    public Providercountry getNumberProviderCountry() {
        return this.numberProviderCountry;
    }

    public void setNumberProviderCountry(Providercountry numberProviderCountry) {
        this.numberProviderCountry = numberProviderCountry;
    }

    @Column(name = "Number")
    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "NumberType", length = 45)
    public String getNumberType() {
        return this.numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    @Column(name = "Restrictions", length = 45)
    public String getRestrictions() {
        return this.restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    @Column(name = "MonthlyRentalRate", length = 45)
    public String getMonthlyRentalRate() {
        return this.monthlyRentalRate;
    }

    public void setMonthlyRentalRate(String monthlyRentalRate) {
        this.monthlyRentalRate = monthlyRentalRate;
    }

    @Column(name = "SetUpRate", length = 45)
    public String getSetUpRate() {
        return this.setUpRate;
    }

    public void setSetUpRate(String setUpRate) {
        this.setUpRate = setUpRate;
    }

    @Column(name = "SmsRate", length = 45)
    public String getSmsRate() {
        return this.smsRate;
    }

    public void setSmsRate(String smsRate) {
        this.smsRate = smsRate;
    }

    @Column(name = "VoicePrice", length = 45)
    public String getVoicePrice() {
        return this.voicePrice;
    }

    public void setVoicePrice(String voicePrice) {
        this.voicePrice = voicePrice;
    }

    @Column(name = "EffectiveDate", length = 45)
    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "ResouceManagerId")
    public Integer getResouceManagerId() {
        return this.resouceManagerId;
    }

    public void setResouceManagerId(Integer resouceManagerId) {
        this.resouceManagerId = resouceManagerId;
    }

    @Column(name = "numbersid", length = 25)
    public String getNumbersid() {
        return numbersid;
    }

    public void setNumbersid(String numbersid) {
        this.numbersid = numbersid;
    }

    @Column(name = "subaccountsid", length = 25)
    public String getSubaccountsid() {
        return subaccountsid;
    }

    public void setSubaccountsid(String subaccountsid) {
        this.subaccountsid = subaccountsid;
    }

    @Column(name = "clientId", length = 25)
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
