package com.imi.rest.model;

import java.util.Set;
import java.util.TreeSet;

public class CountryResponse {

    private MetaForCountries meta;
    private Set<Country> countries = new TreeSet<Country>();

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

    public void addCountries(Set<Country> countrySet) {
        this.countries.addAll(countrySet);
    }

    public CountryResponse() {
    }

}
