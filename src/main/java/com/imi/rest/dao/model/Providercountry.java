package com.imi.rest.dao.model;
// Generated 9 Nov, 2015 4:39:35 PM by Hibernate Tools 4.3.1.Final

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
 * Providercountry generated by hbm2java
 */
@Entity
@Table(name = "providercountry", catalog = "imi")
public class Providercountry implements java.io.Serializable {

    private Integer id;
    private Country country;
    private Provider provider;
    private String services;
    private Set<Purchase> purchases = new HashSet<Purchase>(0);
    private Set<Purchasehistory> purchasehistories = new HashSet<Purchasehistory>(
            0);

    public Providercountry() {
    }

    public Providercountry(Country country, Provider provider, String services,
            Set<Purchase> purchases, Set<Purchasehistory> purchasehistories) {
        this.country = country;
        this.provider = provider;
        this.services = services;
        this.purchases = purchases;
        this.purchasehistories = purchasehistories;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerId")
    public Provider getProvider() {
        return this.provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Column(name = "services", length = 45)
    public String getServices() {
        return this.services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "providercountry")
    public Set<Purchase> getPurchases() {
        return this.purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "providercountry")
    public Set<Purchasehistory> getPurchasehistories() {
        return this.purchasehistories;
    }

    public void setPurchasehistories(Set<Purchasehistory> purchasehistories) {
        this.purchasehistories = purchasehistories;
    }

}
