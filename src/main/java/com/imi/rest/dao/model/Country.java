package com.imi.rest.dao.model;

// Generated 2 Nov, 2015 11:54:20 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Country generated by hbm2java
 */
@Entity
@Table(name = "country", catalog = "imi")
public class Country implements java.io.Serializable {

	private Integer id;
	private String countryIso;
	private String country;
	private String countryCode;
	private Set<Providercountry> providercountries = new HashSet<Providercountry>(
			0);

	public Country() {
	}

	public Country(String countryIso, String country, String countryCode,
			Set<Providercountry> providercountries) {
		this.countryIso = countryIso;
		this.country = country;
		this.countryCode = countryCode;
		this.providercountries = providercountries;
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

	@Column(name = "CountryIso", length = 45)
	public String getCountryIso() {
		return this.countryIso;
	}

	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

	@Column(name = "Country", length = 45)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "CountryCode", length = 45)
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
	public Set<Providercountry> getProvidercountries() {
		return this.providercountries;
	}

	public void setProvidercountries(Set<Providercountry> providercountries) {
		this.providercountries = providercountries;
	}

}
