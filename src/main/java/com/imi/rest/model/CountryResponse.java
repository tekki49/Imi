package com.imi.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.imi.rest.db.model.Countries;

public class CountryResponse {
	
	private MetaForCountries meta;
	private List<Country> countries;
	
	public MetaForCountries getMeta() {
		return meta;
	}

	public void setMeta(MetaForCountries meta) {
		this.meta = meta;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(ArrayList<Country> countries) {
		this.countries = countries;
	}
	public void addCountries (List<Country> countryList){
		this.countries.addAll(countries);
	}
	public CountryResponse()
	{
	}
	

}
