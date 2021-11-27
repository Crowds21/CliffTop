package com.springclifftop.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.springclifftop.domain.entity.anki.Params;

public class AnkiRequest {
    @JSONField(ordinal = 1)
    private String action;
    @JSONField(ordinal = 2)
    private int version;
    @JSONField(ordinal = 3)
    private Params params;


    public AnkiRequest(){
        params = new Params();
    }
    public AnkiRequest(String action,int version){
        this.action = action;
        this.version = version;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }
}
