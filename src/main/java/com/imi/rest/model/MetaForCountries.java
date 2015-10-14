package com.imi.rest.model;

public class MetaForCountries {

	private int page;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}
	public String getFirst_page_url() {
		return first_page_url;
	}
	public void setFirst_page_url(String first_page_url) {
		this.first_page_url = first_page_url;
	}
	public String getPrevious_page_url() {
		return previous_page_url;
	}
	public void setPrevious_page_url(String previous_page_url) {
		this.previous_page_url = previous_page_url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNextPageUrl() {
		return next_page_url;
	}
	public void setNext_page_url(String next_page_url) {
		this.next_page_url = next_page_url;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	private int page_size;
	private String first_page_url;
	private String previous_page_url;
	private String url;
	private String next_page_url;
	private String key;
}
