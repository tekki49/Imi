package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginationUrl {
	private String enabled;

	private String sid;

	private String sip_url;

	private String weight;

	private String trunk_sid;

	private String priority;

	private String date_updated;

	private String date_created;

	private String friendly_name;

	private String url;

	private String account_sid;

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSip_url() {
		return sip_url;
	}

	public void setSip_url(String sip_url) {
		this.sip_url = sip_url;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getTrunk_sid() {
		return trunk_sid;
	}

	public void setTrunk_sid(String trunk_sid) {
		this.trunk_sid = trunk_sid;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDate_updated() {
		return date_updated;
	}

	public void setDate_updated(String date_updated) {
		this.date_updated = date_updated;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public String getFriendly_name() {
		return friendly_name;
	}

	public void setFriendly_name(String friendly_name) {
		this.friendly_name = friendly_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccount_sid() {
		return account_sid;
	}

	public void setAccount_sid(String account_sid) {
		this.account_sid = account_sid;
	}

}