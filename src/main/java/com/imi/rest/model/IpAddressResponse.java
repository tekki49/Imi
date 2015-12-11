package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpAddressResponse {
	private String previous_page_uri;

	private String page_size;

	private String next_page_uri;

	private String page;

	private String start;

	private List<IpAddress> ip_addresses;

	private String first_page_uri;

	private String uri;

	private String end;

	public String getPrevious_page_uri() {
		return previous_page_uri;
	}

	public void setPrevious_page_uri(String previous_page_uri) {
		this.previous_page_uri = previous_page_uri;
	}

	public String getPage_size() {
		return page_size;
	}

	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}

	public String getNext_page_uri() {
		return next_page_uri;
	}

	public void setNext_page_uri(String next_page_uri) {
		this.next_page_uri = next_page_uri;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public List<IpAddress> getIp_addresses() {
		return ip_addresses;
	}

	public void setIp_addresses(List<IpAddress> ip_addresses) {
		this.ip_addresses = ip_addresses;
	}

	public String getFirst_page_uri() {
		return first_page_uri;
	}

	public void setFirst_page_uri(String first_page_uri) {
		this.first_page_uri = first_page_uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}