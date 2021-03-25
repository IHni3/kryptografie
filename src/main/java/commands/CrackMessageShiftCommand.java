package commands;

import configuration.Configuration;

public class CrackMessageShiftCommand extends CrackMessageCommand {

    public CrackMessageShiftCommand(String message) {
        super(message);
    }

    @Override
    public String execute() throws CommandExecutionException {
        return super.execute();
    }

    @Override
    protected String getJarPath() {
        return Configuration.instance.pathToShiftCrackerJavaArchive;
    }

    @Override
    protected String getJarClass() {
        return "ShiftCracker";
    }
}
