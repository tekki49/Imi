package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResponse {
	private String accountBalance;

	public String getAccountBalance() {
		return accountBalance;
	}

	@JsonProperty("accountBalance")
	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	@JsonProperty("value")
	public void setAccountBalanceValue(String value) {
		this.accountBalance = value;
	}
}
