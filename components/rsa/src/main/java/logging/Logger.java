package logging;

import java.io.PrintStream;
import java.time.LocalDateTime;

public class Logger {
    //private LogLevel curLogLevel = LogLevel.ERROR;

    private java.io.PrintStream output;
    private boolean enabled = true;

    public Logger(java.io.PrintStream output) {
        this.output = output;
    }
    public Logger() {
    }

    public void print(String outputString, LogLevel logLevel) {

        if(!enabled) {
            return;
        }

        if(this.output == null) {
            throw new RuntimeException("no output set");
        }

        //if(curLogLevel.getValue() <= logLevel.getValue()) {
            var datetime = LocalDateTime.now();
            output.println("[" + datetime.toString() + "] [" + logLevel.name() + "]: " + outputString);
        //}
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    public void printError(String outputString) {
        print(outputString, LogLevel.ERROR);
    }
    public void printCritical(String outputString) {
        print(outputString, LogLevel.CRITICAL);
    }
    public void printDebug(String outputString) {
        print(outputString, LogLevel.DEBUG);
    }
    public void printInfo(String outputString) {
        print(outputString, LogLevel.INFO);
    }
    public void printWarning(String outputString) {
        print(outputString, LogLevel.WARNING);
    }

    public void enable() {
        enabled = true;
    }
    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
