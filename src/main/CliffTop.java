package main;

import main.api.AnkiAPI;
import main.controller.AnkiNotesController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;


public class JavaAnki {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Personal Integrated Management System");
        System.out.println("Last update date: 2021-10-5");
        Scanner input = new Scanner(System.in);
        System.out.println("1. Add notes to anki");
        System.out.println("--------------------");
        System.out.println("To be continued");
        System.out.println("--------------------");

        var item = input.next();

        switch (item){
            case "1":
                try {
                    AnkiNotesController.createNotes();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }






}