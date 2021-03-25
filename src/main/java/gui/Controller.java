package gui;


import commands.CommandExecutionException;
import commands.ICommand;
import configuration.Configuration;
import logging.Logger;
import parser.IParser;
import parser.Parser;
import parser.ParserException;

import java.io.File;

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

    public void showLog(){

    }

    public void disableDebugging(){
        displayText("Logging turned: On");
        configuration.instance.debugModeEnabled = false;
    }

    public void enableDebugging(){
        displayText("Logging turned: Off");
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

    public boolean isDebuggingEnabled(){
        return Configuration.instance.debugModeEnabled;
    }
}
