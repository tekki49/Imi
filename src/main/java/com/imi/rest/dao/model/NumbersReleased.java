package com.imi.rest.dao.model;

// Generated 25 Nov, 2015 2:51:32 PM by Hibernate Tools 4.3.1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ResourceMaster generated by hbm2java
 */
@Entity
@Table(name = "numbers_released", schema = "ump_resource_mgr")
public class NumbersReleased implements java.io.Serializable {

    private Integer id;
    private String serviceCode;
    private byte channel;
    private byte status;
    private String userKey;
    private int provider;
    private String countryIso;
    private String monthlyRental;
    private String createdOn;
    private String releasedOn;

    public NumbersReleased() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "service_code", nullable = false, length = 30)
    public String getServiceCode() {
        return this.serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @Column(name = "channel", nullable = false)
    public byte getChannel() {
        return this.channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    @Column(name = "status", nullable = false)
    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Column(name = "user_key", length = 45)
    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @Column(name = "provider")
    public int getProvider() {
        return provider;
    }

    public void setProvider(int provider) {
        this.provider = provider;
    }

    @Column(name = "country_iso", length = 10)
    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    @Column(name = "monthly_rental", length = 50)
    public String getMonthlyRental() {
        return monthlyRental;
    }

    public void setMonthlyRental(String monthlyRental) {
        this.monthlyRental = monthlyRental;
    }

    @Column(name = "created_on", length = 25)
    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "released_on", length = 25)
    public String getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(String releasedOn) {
        this.releasedOn = releasedOn;
    }

}
