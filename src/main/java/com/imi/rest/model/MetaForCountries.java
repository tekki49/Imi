package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaForCountries {

	private int page;
	private int pageSize;
	private String firstPageUrl;
	private String previousPageUrl;
	private String url;
	private String nextPageUrl;
	private String key;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@JsonProperty("page_size")
	public int getPageSize() {
		return pageSize;
	}

	@JsonProperty("page_size")
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@JsonProperty("first_page_url")
	public String getFirstPageUrl() {
		return firstPageUrl;
	}

	@JsonProperty("first_page_url")
	public void setFirstPageUrl(String firstPageUrl) {
		this.firstPageUrl = firstPageUrl;
	}

	@JsonProperty("previous_page_url")
	public String getPreviousPageUrl() {
		return previousPageUrl;
	}

	@JsonProperty("previous_page_url")
	public void setPreviousPageUrl(String previousPageUrl) {
		this.previousPageUrl = previousPageUrl;
	}

	@JsonProperty("next_page_url")
	public String getNextPageUrl() {
		return nextPageUrl;
	}

	@JsonProperty("next_page_url")
	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
