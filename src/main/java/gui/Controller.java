/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

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
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Controller {

    private GUI gui;
    private Configuration configuration = Configuration.instance;
    private IParser parser;

    public Controller(GUI gui) {
        this.gui = gui;
        this.parser = new Parser();

        Configuration.instance.setGUILogger(new Logger(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                displayText("" + ((char)b));
            }
            @Override
            public void write(byte[] b) {
                displayText(new String(b));
            }
            @Override
            public void write(byte[] b, int off, int len) {
                displayText(new String(b, off, len));
            }
        })));
    }

    public void closeGUI(){
        System.exit(0);
    }

    public void displayText(String text){
        gui.setOutputText(text);
    }

    public void disableDebugging(){
        Configuration.instance.getGUILogger().printInfo("Logging turned: off");
        configuration.instance.debugModeEnabled = false;
    }

    public void enableDebugging(){
        Configuration.instance.getGUILogger().printInfo("Logging turned: on");
        configuration.instance.debugModeEnabled = true;
    }

    public void executeCommand(String inputString){
        try {
            ICommand command = parser.parse(inputString);
            var result = command.execute();
            if (result != null)
                Configuration.instance.getGUILogger().printInfo(result);
        } catch (ParserException e) {
            Configuration.instance.getGUILogger().printWarning("Could not parse command!");
            e.printStackTrace();
            //TODO
        } catch (CommandExecutionException e) {
            Configuration.instance.getGUILogger().printWarning(e.getMessage());
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
