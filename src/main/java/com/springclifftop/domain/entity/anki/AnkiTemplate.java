package com.springclifftop.domain.entity.anki;

import com.alibaba.fastjson.annotation.JSONField;

public class AnkiTemplate {
    @JSONField(name = "Name")
    String Name;
    @JSONField(name = "Front")
    String Front;
    @JSONField(name = "Back")
    String Back;

    public AnkiTemplate() {
    }

    public AnkiTemplate(String name, String front, String back) {
        Name = name;
        Front = front;
        Back = back;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFront() {
        return Front;
    }

    public void setFront(String front) {
        Front = front;
    }

    public String getBack() {
        return Back;
    }

    public void setBack(String back) {
        Back = back;
    }
}
