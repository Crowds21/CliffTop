package main;

import main.api.AnkiAPI;
import main.common.enums.TerminalCommand;
import main.controller.AnkiNotesController;
import main.controller.SiYuanDataViewController;
import main.utils.TerminalUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import static java.lang.System.exit;
import static main.utils.TerminalUtils.terminalOutputWithBrown;


public class CliffTop {

    public static void main(String[] args) throws Exception {
        System.out.println("Personal Integrated Management System");
        System.out.println("Last update date: 2021-10-5");
        Scanner input = new Scanner(System.in);
        String terminalInput;
        do {
            terminalOutputWithBrown("CliffTop>");
            terminalInput = input.nextLine();
            String[] getInput = terminalInput.split(" -");
            if (getInput.length < 2) {
            } else {
                String command = getInput[0].trim();
                String parameter = getInput[1].trim();
                try {
                    TerminalCommand.valueOf(command).operation(parameter);
                } catch (IllegalArgumentException e) {
                    TerminalUtils.terminalOutputWithRed("CliffTop:" + command + ": command not fond\n");
                }
            }
        } while (!terminalInput.equals("exit"));

        TerminalUtils.terminalOutputWithBlue("Experience is the best teacher.");
        input.close();
    }

}