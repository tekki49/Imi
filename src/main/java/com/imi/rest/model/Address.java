package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

	private String sid;
	private String account_sid;
	private String friendly_name;
	private String customer_name;
	private String street;
	private String city;
	private String region;
	private String postal_code;
	private String iso_country;
	private String uri;
	private String date_created;
	private String date_updated;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getAccount_sid() {
		return account_sid;
	}

	public void setAccount_sid(String account_sid) {
		this.account_sid = account_sid;
	}

	public String getFriendly_name() {
		return friendly_name;
	}

	public void setFriendly_name(String friendly_name) {
		this.friendly_name = friendly_name;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getIso_country() {
		return iso_country;
	}

	public void setIso_country(String iso_country) {
		this.iso_country = iso_country;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@JsonProperty("Sid")
	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public String getDate_updated() {
		return date_updated;
	}

	public void setDate_updated(String date_updated) {
		this.date_updated = date_updated;
	}

}
