package com.imi.rest.model;

import com.imi.rest.db.model.Providers;

public class ProviderResponse {

	private String identifier;
	private String api_key;
	private String auth_Id;
	private String provider;

	public ProviderResponse() {
	}

	public ProviderResponse(Providers providers) {
		this.api_key = providers.getApiKey();
		this.auth_Id = providers.getAuthId();
		this.provider = providers.getName();
		// TODO need to change the logic here
		this.identifier = providers.getName();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	public String getAuth_Id() {
		return auth_Id;
	}

	public void setAuth_Id(String auth_Id) {
		this.auth_Id = auth_Id;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}
