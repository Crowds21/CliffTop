package test;

import main.controller.AnkiNotesController;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class AnkiNotesControllerTest {

    @Test
    void createNotes() throws Exception {
        AnkiNotesController.createNotes();
    }

    @Test
    void getModelInfo() throws URISyntaxException, IOException, InterruptedException {
        AnkiNotesController.getModelInfo();
    }
}