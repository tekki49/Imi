package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country implements Comparable<Country> {

    private String country;
    private String isoCountry;

    // private String url;
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("iso_country")
    public String getIsoCountry() {
        return isoCountry;
    }

    @JsonProperty("iso_country")
    public void setIsoCountry(String isoCountry) {
        this.isoCountry = isoCountry;
    }

    @Override
    public boolean equals(Object country) {
        if (country instanceof Country) {
            Country country1 = (Country) country;
            return isoCountry.equalsIgnoreCase(country1.getIsoCountry());
        }
        return false;
    }

    @Override
    public int compareTo(Country c) {
        return this.getCountry().compareTo(c.getCountry());
    }
}
