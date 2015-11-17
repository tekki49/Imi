package com.imi.rest.dao.model;
// Generated 9 Nov, 2015 4:39:35 PM by Hibernate Tools 4.3.1.Final

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
 * Purchasehistory generated by hbm2java
 */
@Entity
@Table(name = "purchasehistory", schema = "ump_resource_mgr")
public class Purchasehistory implements java.io.Serializable {

    private Integer id;
    private Providercountry providercountry;
    private Provisioning provisioning;
    private Integer number;
    private String numberType;
    private String restrictions;
    private String monthlyRentalRate;
    private String setUpRate;
    private String smsPrice;
    private String voicePrice;
    private String startDate;
    private String endDate;
    private Integer resourceManagerId;

    public Purchasehistory() {
    }

    public Purchasehistory(Providercountry providercountry,
            Provisioning provisioning, Integer number, String numberType,
            String restrictions, String monthlyRentalRate, String setUpRate,
            String smsPrice, String voicePrice, String startDate,
            String endDate, Integer resourceManagerId) {
        this.providercountry = providercountry;
        this.provisioning = provisioning;
        this.number = number;
        this.numberType = numberType;
        this.restrictions = restrictions;
        this.monthlyRentalRate = monthlyRentalRate;
        this.setUpRate = setUpRate;
        this.smsPrice = smsPrice;
        this.voicePrice = voicePrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resourceManagerId = resourceManagerId;
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
    public Providercountry getProvidercountry() {
        return this.providercountry;
    }

    public void setProvidercountry(Providercountry providercountry) {
        this.providercountry = providercountry;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provisioningId")
    public Provisioning getProvisioning() {
        return this.provisioning;
    }

    public void setProvisioning(Provisioning provisioning) {
        this.provisioning = provisioning;
    }

    @Column(name = "Number")
    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
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

    @Column(name = "SmsPrice", length = 45)
    public String getSmsPrice() {
        return this.smsPrice;
    }

    public void setSmsPrice(String smsPrice) {
        this.smsPrice = smsPrice;
    }

    @Column(name = "VoicePrice", length = 45)
    public String getVoicePrice() {
        return this.voicePrice;
    }

    public void setVoicePrice(String voicePrice) {
        this.voicePrice = voicePrice;
    }

    @Column(name = "StartDate", length = 45)
    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Column(name = "EndDate", length = 45)
    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Column(name = "ResourceManagerId")
    public Integer getResourceManagerId() {
        return this.resourceManagerId;
    }

    public void setResourceManagerId(Integer resourceManagerId) {
        this.resourceManagerId = resourceManagerId;
    }

}
