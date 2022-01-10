package com.springclifftop.domain.entity.anki;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class Note {
    @JSONField(ordinal = 1)
    String deckName;
    @JSONField(ordinal = 2)
    String modelName;
    @JSONField(ordinal = 3)
    Fields fields;
    @JSONField(ordinal = 4,serialize=true)
    ArrayList<String> tags;

    public Note() {
        fields = new Fields();
        tags = new ArrayList<String>();
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setTags(String tag) {
        this.tags.add(tag);
    }


}
