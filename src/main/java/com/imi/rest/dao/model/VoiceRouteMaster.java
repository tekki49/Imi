package com.imi.rest.dao.model;

// Generated 25 Nov, 2015 2:51:32 PM by Hibernate Tools 4.3.1

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * VoiceRouteMaster generated by hbm2java
 */
@Entity
@Table(name = "voice_route_master", schema = "ump_resource_mgr", uniqueConstraints = @UniqueConstraint(columnNames = {
		"site", "route", "provider_id", "country_code" }) )
public class VoiceRouteMaster implements java.io.Serializable {

	private Integer id;
	private String site;
	private String route;
	private Integer providerId;
	private String countryCode;
	private String countryIso;
	private String dvpCallbackUrl;

	public VoiceRouteMaster() {
	}

	public VoiceRouteMaster(String site, String route, Integer providerId, String countryCode, String dvpCallbackUrl) {
		this.site = site;
		this.route = route;
		this.providerId = providerId;
		this.countryCode = countryCode;
		this.dvpCallbackUrl = dvpCallbackUrl;
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

	@Column(name = "site", length = 50)
	public String getSite() {
		return this.site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Column(name = "route", length = 50)
	public String getRoute() {
		return this.route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	@Column(name = "provider_id")
	public Integer getProviderId() {
		return this.providerId;
	}

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

	@Column(name = "country_iso", length = 10)
	public String getCountryIso() {
		return this.countryIso;
	}

	public void setCountryIso(String countryIso) {
		this.countryIso = countryIso;
	}

	@Column(name = "country_code", length = 10)
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column(name = "voice_callback_url", length = 65535)
	public String getDvpCallbackUrl() {
		return this.dvpCallbackUrl;
	}

	public void setDvpCallbackUrl(String dvpCallbackUrl) {
		this.dvpCallbackUrl = dvpCallbackUrl;
	}

}
