package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginatingUrlResponse {

	private List<OriginationUrl> origination_urls;

	private Meta meta;

	public List<OriginationUrl> getOrigination_urls() {
		return origination_urls;
	}

	public void setOrigination_urls(List<OriginationUrl> origination_urls) {
		this.origination_urls = origination_urls;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

}
