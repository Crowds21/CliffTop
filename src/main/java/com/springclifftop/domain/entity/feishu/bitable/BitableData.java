package com.springclifftop.domain.entity.feishu.bitable;

import java.util.ArrayList;

public class BitableData {

    BitableApp app;
    boolean has_more;
    String page_token;
    int total;
    ArrayList<BiRecord> items;


    public BitableApp getApp() {
        return app;
    }

    public void setApp(BitableApp app) {
        this.app = app;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public String getPage_token() {
        return page_token;
    }

    public void setPage_token(String page_token) {
        this.page_token = page_token;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<BiRecord> getItems() {
        return items;
    }

    public void setItems(ArrayList<BiRecord> items) {
        this.items = items;
    }
}
