package gui;


import commands.ICommand;
import configuration.Configuration;
import parser.IParser;
import parser.Parser;
import parser.ParserException;

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

    public void disableLogging(){
        displayText("Logging turned: On");
        configuration.enableLogging();
    }

    public void enableLogging(){
        displayText("Logging turned: Off");
        configuration.disableLogging();
    }

    public void executeCommand(String inputString){

        try {
            ICommand command = parser.parse(inputString);
            command.execute();
        } catch (ParserException ex) {
            ex.printStackTrace();
            //TODO
        }

    }

    public boolean isLoggingEnabled(){
        return true;
    }
}
