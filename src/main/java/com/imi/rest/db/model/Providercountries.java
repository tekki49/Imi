package com.imi.rest.db.model;

// Generated Oct 6, 2015 6:50:08 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Providercountries generated by hbm2java
 */
@Entity
@Table(name = "providercountries", catalog = "imi")
public class Providercountries implements java.io.Serializable {

	private int id;
	private Providers providers;
	private Countries countries;
	private String services;
	private Set<Purchases> purchaseses = new HashSet<Purchases>(0);
	private Set<Purchasehistories> purchasehistorieses = new HashSet<Purchasehistories>(
			0);

	public Providercountries() {
	}

	public Providercountries(int id) {
		this.id = id;
	}

	public Providercountries(int id, Providers providers, Countries countries,
			String services, Set<Purchases> purchaseses,
			Set<Purchasehistories> purchasehistorieses) {
		this.id = id;
		this.providers = providers;
		this.countries = countries;
		this.services = services;
		this.purchaseses = purchaseses;
		this.purchasehistorieses = purchasehistorieses;
	}

	@Id
	@Column(name = "Id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "providerId")
	public Providers getProviders() {
		return this.providers;
	}

	public void setProviders(Providers providers) {
		this.providers = providers;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "countryId")
	public Countries getCountries() {
		return this.countries;
	}

	public void setCountries(Countries countries) {
		this.countries = countries;
	}

	@Column(name = "services", length = 45)
	public String getServices() {
		return this.services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "providercountries")
	public Set<Purchases> getPurchaseses() {
		return this.purchaseses;
	}

	public void setPurchaseses(Set<Purchases> purchaseses) {
		this.purchaseses = purchaseses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "providercountries")
	public Set<Purchasehistories> getPurchasehistorieses() {
		return this.purchasehistorieses;
	}

	public void setPurchasehistorieses(
			Set<Purchasehistories> purchasehistorieses) {
		this.purchasehistorieses = purchasehistorieses;
	}

}