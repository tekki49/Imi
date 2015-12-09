package com.imi.rest.model;

import java.util.List;

public class SipTrunkResponse {

    private List<SipTrunk> trunks;

    private Meta meta;

    public List<SipTrunk> getTrunks() {
        return trunks;
    }

    public void setTrunks(List<SipTrunk> trunks) {
        this.trunks = trunks;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
