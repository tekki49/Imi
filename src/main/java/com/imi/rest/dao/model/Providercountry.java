package com.imi.rest.dao.model;

// Generated 25 Nov, 2015 2:51:32 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * NumberProviderCountry generated by hbm2java
 */
@Entity
@Table(name = "providercountry", schema = "ump_resource_mgr")
public class Providercountry implements java.io.Serializable {

    private Integer id;
    private Provider numberProvider;
    private Country resourceCountry;
    private String services;
    private Set<Purchase> numberPurchases = new HashSet<Purchase>(0);
    private Set<PurchaseHistory> numberPurchaseHistories = new HashSet<PurchaseHistory>(
            0);

    public Providercountry() {
    }

    public Providercountry(Provider numberProvider, Country resourceCountry,
            String services, Set<Purchase> numberPurchases,
            Set<PurchaseHistory> numberPurchaseHistories) {
        this.numberProvider = numberProvider;
        this.resourceCountry = resourceCountry;
        this.services = services;
        this.numberPurchases = numberPurchases;
        this.numberPurchaseHistories = numberPurchaseHistories;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    public Provider getNumberProvider() {
        return this.numberProvider;
    }

    public void setNumberProvider(Provider numberProvider) {
        this.numberProvider = numberProvider;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    public Country getResourceCountry() {
        return this.resourceCountry;
    }

    public void setResourceCountry(Country resourceCountry) {
        this.resourceCountry = resourceCountry;
    }

    @Column(name = "services", length = 45)
    public String getServices() {
        return this.services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "numberProviderCountry")
    public Set<Purchase> getNumberPurchases() {
        return this.numberPurchases;
    }

    public void setNumberPurchases(Set<Purchase> numberPurchases) {
        this.numberPurchases = numberPurchases;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "numberProviderCountry")
    public Set<PurchaseHistory> getNumberPurchaseHistories() {
        return this.numberPurchaseHistories;
    }

    public void setNumberPurchaseHistories(
            Set<PurchaseHistory> numberPurchaseHistories) {
        this.numberPurchaseHistories = numberPurchaseHistories;
    }

}
