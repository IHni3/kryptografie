package commands;

public interface ICommand {
    String execute() throws CommandExecutionException;
}
