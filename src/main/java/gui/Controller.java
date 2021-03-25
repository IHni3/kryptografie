package gui;


import commands.CommandExecutionException;
import commands.ICommand;
import configuration.Configuration;
import filesystem.FileSystemUtils;
import logging.Logger;
import parser.IParser;
import parser.Parser;
import parser.ParserException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Controller {

    private GUI gui;
    private Configuration configuration = Configuration.instance;
    private IParser parser;

    public Controller(GUI gui) {
        this.gui = gui;
        this.parser = new Parser();
    }

    public void closeGUI(){
        System.exit(0);
    }

    public void displayText(String text){
        gui.setOutputText(text);
    }

    public void disableDebugging(){
        displayText("Logging turned: off");
        configuration.instance.debugModeEnabled = false;
    }

    public void enableDebugging(){
        displayText("Logging turned: on");
        configuration.instance.debugModeEnabled = true;
    }

    public void executeCommand(String inputString){

        try {
            ICommand command = parser.parse(inputString);
            command.execute();
        } catch (ParserException e) {
            e.printStackTrace();
            //TODO
        } catch (CommandExecutionException e) {
            e.printStackTrace();
        }

    }

    public void outputLastLogFile() {
        File dir = new File(Configuration.instance.logsDirectory);
        try {
            var file = FileSystemUtils.getLastCreatedFile(dir);
            var fileContent = Files.readString(file.toPath(), StandardCharsets.US_ASCII);
            displayText(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
            displayText("could not read last file from \"" + Configuration.instance.logsDirectory + "\"!");
        }
    }



    public boolean isDebuggingEnabled(){
        return Configuration.instance.debugModeEnabled;
    }
}
