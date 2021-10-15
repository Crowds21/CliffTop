package main.common.enums;

import main.controller.AnkiNotesController;

public enum AnkiCommandParam implements TermianlArgsEnum {

    addNotes() {
        @Override
        public String operation(String args) {
            try {
                AnkiNotesController.createNotes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };



}
