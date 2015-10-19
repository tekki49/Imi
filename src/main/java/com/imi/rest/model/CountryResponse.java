package com.imi.rest.model;

import java.util.HashSet;
import java.util.Set;

public class CountryResponse {

    private MetaForCountries meta;
    private Set<Country> countries = new HashSet<Country>();

    public MetaForCountries getMeta() {
        return meta;
    }

    public void setMeta(MetaForCountries meta) {
        this.meta = meta;
    }

    public Set<Country> getCountries() {
        return countries;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }

    public void addCountries(Set<Country> countryList) {
        this.countries.addAll(countries);
    }

    public CountryResponse() {
    }

}
