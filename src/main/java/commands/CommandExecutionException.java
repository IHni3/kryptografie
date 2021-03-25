package commands;

public class CommandExecutionException extends Exception{
    public  CommandExecutionException(String message){
        super(message);
    }

    public  CommandExecutionException() {};
}
