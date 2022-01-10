package com.springclifftop.domain.entity.feishu.bitable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BiRecord {
    String id;
    BiProjectFields fields;
    //HashMap<String,String> fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BiProjectFields getFields() {
        return fields;
    }

    public void setFields(BiProjectFields fields) {
        this.fields = fields;
    }


//    public HashMap<String, String> getFields() {
//        return fields;
//    }
//
//    public void setFields(HashMap<String, String> fields) {
//        this.fields = fields;
//    }
}
