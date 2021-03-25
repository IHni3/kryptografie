package commands;

import components.ComponentUtils;

public abstract class CrackMessageCommand implements ICommand{

    private final String message;

    public CrackMessageCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String execute() throws CommandExecutionException {
        return crackMessage(getJarPath(),getJarClass());
    }

    private String crackMessage(String jarPath, String className) throws CommandExecutionException {
        try {
            var port = ComponentUtils.getPortFromJar(jarPath, className);
            var method = port.getClass().getDeclaredMethod("decrypt", String.class);
            String decryptedMessage = String.valueOf(method.invoke(port, getMessage()));
            return decryptedMessage;
        } catch (Exception exception) {
            throw new CommandExecutionException();
        }
    }

    protected abstract String getJarPath();
    protected abstract String getJarClass();
}
