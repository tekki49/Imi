package com.imi.rest.model;

import java.util.List;

public class TwilioAddressResponse {

    private String first_page_uri;
    private String end;
    private List<Address> addresses;
    private String previous_page_uri;
    private String uri;
    private int page_size;
    private int start;
    private String next_page_uri;
    private int page;

    public String getFirst_page_uri() {
        return first_page_uri;
    }

    public void setFirst_page_uri(String first_page_uri) {
        this.first_page_uri = first_page_uri;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
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

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
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

}
