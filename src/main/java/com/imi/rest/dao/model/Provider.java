package com.imi.rest.dao.model;

// Generated 2 Nov, 2015 11:07:48 AM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Provider generated by hbm2java
 */
@Entity
@Table(name = "provider", catalog = "imi")
public class Provider implements java.io.Serializable {

	private int id;
	private String apiKey;
	private String authId;
	private String name;
	private Set<Providercountry> providercountries = new HashSet<Providercountry>(
			0);

	public Provider() {
	}

	public Provider(int id) {
		this.id = id;
	}

	public Provider(int id, String apiKey, String authId, String name,
			Set<Providercountry> providercountries) {
		this.id = id;
		this.apiKey = apiKey;
		this.authId = authId;
		this.name = name;
		this.providercountries = providercountries;
	}

	@Id
	@Column(name = "Id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "apiKey", length = 45)
	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Column(name = "authId", length = 45)
	public String getAuthId() {
		return this.authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	@Column(name = "name", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provider")
	public Set<Providercountry> getProvidercountries() {
		return this.providercountries;
	}

	public void setProvidercountries(Set<Providercountry> providercountries) {
		this.providercountries = providercountries;
	}

}
