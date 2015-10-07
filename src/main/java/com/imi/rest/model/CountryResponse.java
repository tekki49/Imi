package com.imi.rest.model;

import com.imi.rest.db.model.Countries;

public class CountryResponse {

	private String country_iso_code;
	private String country_name;
	private String provider;
	
	public CountryResponse()
	{
	}
	
	public CountryResponse(Countries countries)
	{
		this.country_iso_code=countries.getCountryIsoCode();
		this.country_name=countries.getCountry();
		//TODO change the logic here
		this.provider=null;
	}

	public String getCountry_iso_code() {
		return country_iso_code;
	}

	public void setCountry_iso_code(String country_iso_code) {
		this.country_iso_code = country_iso_code;
	}

	public String getCountry_name() {
		return country_name;
	}

	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}
