package com.springclifftop.domain.entity.feishu.bitable;

import java.util.ArrayList;

public class BitableApp {
    String app_token;
    //ArrayList<BiTable> tables;
    String name;
    String revision;


    public String getApp_token() {
        return app_token;
    }

    public void setApp_token(String app_token) {
        this.app_token = app_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }
}
