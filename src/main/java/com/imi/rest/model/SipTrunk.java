package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SipTrunk {
	private String sid;

	private Recording recording;

	private String[] auth_type_set;

	private String auth_type;

	private String secure;

	private String date_created;

	private String friendly_name;

	private String url;

	private String account_sid;

	private String disaster_recovery_method;

	private String date_updated;

	private String domain_name;

	private String disaster_recovery_url;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Recording getRecording() {
		return recording;
	}

	public void setRecording(Recording recording) {
		this.recording = recording;
	}

	public String[] getAuth_type_set() {
		return auth_type_set;
	}

	public void setAuth_type_set(String[] auth_type_set) {
		this.auth_type_set = auth_type_set;
	}

	public String getAuth_type() {
		return auth_type;
	}

	public void setAuth_type(String auth_type) {
		this.auth_type = auth_type;
	}

	public String getSecure() {
		return secure;
	}

	public void setSecure(String secure) {
		this.secure = secure;
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

	public String getDisaster_recovery_method() {
		return disaster_recovery_method;
	}

	public void setDisaster_recovery_method(String disaster_recovery_method) {
		this.disaster_recovery_method = disaster_recovery_method;
	}

	public String getDate_updated() {
		return date_updated;
	}

	public void setDate_updated(String date_updated) {
		this.date_updated = date_updated;
	}

	public String getDomain_name() {
		return domain_name;
	}

	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}

	public String getDisaster_recovery_url() {
		return disaster_recovery_url;
	}

	public void setDisaster_recovery_url(String disaster_recovery_url) {
		this.disaster_recovery_url = disaster_recovery_url;
	}

}