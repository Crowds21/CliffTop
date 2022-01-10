package com.springclifftop.domain.entity.anki;

import com.alibaba.fastjson.annotation.JSONField;

public class Fields {
    @JSONField(name="Front",ordinal = 1)
    String front;
    @JSONField(name="Back",ordinal = 2)
    String back;
    @JSONField(name="Extra",ordinal = 3)
    String extra;
    @JSONField(name="Backup",ordinal = 4)
    String backup;
    @JSONField(name="SiyuanID",ordinal = 5)
    String siyuanID;


    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSiyuanID() {
        return siyuanID;
    }

    public void setSiyuanID(String siyuanID) {
        this.siyuanID = siyuanID;
    }
}
