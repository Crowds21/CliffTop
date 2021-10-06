package main.entity.anki.params;

import main.entity.anki.params.Note;

import java.util.ArrayList;

public class Params {
    private String modelName;
    private String deck;
    private Boolean isCloze;
    private ArrayList<String> inOrderFields;
    private ArrayList<AnkiTemplate> cardTemplates;
    private ArrayList<Note> notes;


    public Params(){
        notes = new ArrayList<>();
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
        return isCloze;
    }

    public void setCloze(Boolean cloze) {
        isCloze = cloze;
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
}
