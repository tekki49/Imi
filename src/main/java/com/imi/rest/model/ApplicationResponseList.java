package com.imi.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationResponseList {
    private String first_page_uri;
    private String next_page_uri;
    private int page;
    private int page_size;
    private String previous_page_uri;
    private String uri;
    private List<ApplicationResponse> objects;
    private int start;
    private int end;

    public String getFirst_page_uri() {
        return first_page_uri;
    }

    public void setFirst_page_uri(String first_page_uri) {
        this.first_page_uri = first_page_uri;
    }

    public String getNext_page_uri() {
        return next_page_uri;
    }

    public void setNext_page_uri(String next_page_uri) {
        this.next_page_uri = next_page_uri;
    }

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

    public String getPrevious_page_uri() {
        return previous_page_uri;
    }

    public void setPrevious_page_uri(String previous_page_uri) {
        this.previous_page_uri = previous_page_uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<ApplicationResponse> getObjects() {
        return objects;
    }

    public void setObjects(List<ApplicationResponse> objects) {
        this.objects = objects;
    }

    @JsonProperty("incoming_phone_numbers")
    public void setIncoming_phone_numbers(List<ApplicationResponse> objects) {
        this.objects = objects;
    }

}
