package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Country {

	private String country;
	private String iso_country;
//	private String url;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getIso_country() {
		return iso_country;
	}
	public void setIso_country(String iso_country) {
		this.iso_country = iso_country;
	}
	@Override
	public boolean  equals(Object country){
		if( country instanceof Country ){
			Country country1 = (Country) country; 
			return iso_country.equalsIgnoreCase( country1.getIso_country() );
		}
		return false;
	}
//	public String getUrl() {
//		return url;
//	}
//	public void setUrl(String url) {
//		this.url = url;
//	}
}
