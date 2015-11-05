package com.imi.rest.model;

import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryResponse {

    private Integer id;
    private String countryIso;
    private String countryCode;
    private String country;
    private MetaForCountries meta;
    private Set<Country> countries = new TreeSet<Country>();

    @JsonIgnore
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getCountryIso() {
        return countryIso;
    }

    @JsonIgnore
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @JsonIgnore
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public MetaForCountries getMeta() {
        return meta;
    }

    @JsonProperty("meta")
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
        if (countrySet != null) {
            this.countries.addAll(countrySet);
        }
    }

    public CountryResponse() {
    }

}
