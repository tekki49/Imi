package com.imi.rest.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryPricing {

    private String country;
    private String countryIsoCode;
    private Map<String, Map<String, String>> pricing = new HashMap<String, Map<String, String>>();
    private List<OutBoundPrefixPrice> outbound_prefix_prices;
    private List<InboundCallPrice> inbound_call_prices;
    private String price_unit;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    @JsonProperty("countryIsoCode")
    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    @JsonProperty("iso_country")
    public void setIsoCountryCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public Map<String, Map<String, String>> getPricing() {
        return pricing;
    }

    public void setPricing(Map<String, Map<String, String>> pricing) {
        this.pricing = pricing;
    }

    @JsonIgnore
    public List<OutBoundPrefixPrice> getOutbound_prefix_prices() {
        return outbound_prefix_prices;
    }

    @JsonProperty("outbound_prefix_prices")
    public void setOutbound_prefix_prices(
            List<OutBoundPrefixPrice> outbound_prefix_prices) {
        this.outbound_prefix_prices = outbound_prefix_prices;
    }

    @JsonIgnore
    public List<InboundCallPrice> getInbound_call_prices() {
        return inbound_call_prices;
    }

    @JsonProperty("inbound_call_prices")
    public void setInbound_call_prices(
            List<InboundCallPrice> inbound_call_prices) {
        this.inbound_call_prices = inbound_call_prices;
    }

    @JsonIgnore
    public String getPrice_unit() {
        return price_unit;
    }

    @JsonProperty("price_unit")
    public void setPrice_unit(String price_unit) {
        this.price_unit = price_unit;
    }

}
