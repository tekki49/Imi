package com.imi.rest.model;

public class PlivoAccountResponse {
	private String account_type;
	private String address;
	private String api_id;
	private String auth_id;
	private Boolean auto_recharge;
	private String cash_credits;
	private String city;
	private String name;
	private String resource_uri;
	private String state;
	private String timezone;
	private String billing_mode;
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getApi_id() {
		return api_id;
	}
	public void setApi_id(String api_id) {
		this.api_id = api_id;
	}
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	public Boolean getAuto_recharge() {
		return auto_recharge;
	}
	public void setAuto_recharge(Boolean auto_recharge) {
		this.auto_recharge = auto_recharge;
	}
	public String getCash_credits() {
		return cash_credits;
	}
	public void setCash_credits(String cash_credits) {
		this.cash_credits = cash_credits;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResource_uri() {
		return resource_uri;
	}
	public void setResource_uri(String resource_uri) {
		this.resource_uri = resource_uri;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getBilling_mode() {
		return billing_mode;
	}
	public void setBilling_mode(String billing_mode) {
		this.billing_mode = billing_mode;
	}

}
