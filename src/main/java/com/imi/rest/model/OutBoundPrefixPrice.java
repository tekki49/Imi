package com.imi.rest.model;

import java.util.List;

public class OutBoundPrefixPrice {

	private List<String> prefixes;
	private String friendly_name;
	private String base_price;
	private String current_price;

	public List<String> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(List<String> prefixes) {
		this.prefixes = prefixes;
	}

	public String getFriendly_name() {
		return friendly_name;
	}

	public void setFriendly_name(String friendly_name) {
		this.friendly_name = friendly_name;
	}

	public String getBase_price() {
		return base_price;
	}

	public void setBase_price(String base_price) {
		this.base_price = base_price;
	}

	public String getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(String current_price) {
		this.current_price = current_price;
	}

}
