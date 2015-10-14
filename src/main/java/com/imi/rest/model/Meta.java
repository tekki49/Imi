package com.imi.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Meta {
	private String limit;
	private String offset;
	private String previous;
	private String next;
	private int total;

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	@JsonIgnore
	public int getTotal() {
		return total;
	}

	@JsonProperty("total")
	public void setTotal(int total) {
		this.total = total;
	}
	
	@JsonProperty("total_count")
	public void set_total_count(int total) {
		this.total = total;
	}
	

}
