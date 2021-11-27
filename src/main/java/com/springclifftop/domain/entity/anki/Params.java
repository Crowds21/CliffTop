package com.springclifftop.domain.entity.anki;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class Params {
    @JSONField(ordinal = 1)
    private String modelName;
    @JSONField(ordinal = 2)
    private String deck;
    @JSONField(name="isCloze",ordinal = 3)
    private Boolean cloze;
    @JSONField(ordinal = 4)
    private ArrayList<String> inOrderFields;
    @JSONField(ordinal = 5)
    private ArrayList<AnkiTemplate> cardTemplates;
    @JSONField(ordinal = 6)
    private ArrayList<Note> notes;
    @JSONField(ordinal = 7)
    private String css;

    public Params(){
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public Boolean getCloze() {
        return cloze;
    }

    public void setCloze(Boolean cloze) {
        cloze = cloze;
    }

    public ArrayList<String> getInOrderFields() {
        return inOrderFields;
    }

    public void setInOrderFields(ArrayList<String> inOrderFields) {
        this.inOrderFields = inOrderFields;
    }

    public ArrayList<AnkiTemplate> getCardTemplates() {
        return cardTemplates;
    }

    public void setCardTemplates(ArrayList<AnkiTemplate> cardTemplates) {
        this.cardTemplates = cardTemplates;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }
}
