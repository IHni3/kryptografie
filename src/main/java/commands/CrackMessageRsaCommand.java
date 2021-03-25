package commands;

import components.ComponentUtils;
import configuration.Configuration;

public class CrackMessageRsaCommand implements ICommand {
    private final String message;
    private final String keyfile;

    public CrackMessageRsaCommand(String message, String keyfile) {
        this.message = message;
        this.keyfile = keyfile;
    }

    public String getMessage() {
        return message;
    }

    public String getKeyfile() {
        return keyfile;
    }

    private String crackMessage(String jarPath, String className) throws CommandExecutionException {
        try {
            var port = ComponentUtils.getPortFromJar(jarPath, className);
            var method = port.getClass().getDeclaredMethod("decrypt", String.class, String.class);
            String decryptedMessage = String.valueOf(method.invoke(port, getMessage(), getKeyfile()));

            return decryptedMessage;
        } catch (Exception exception) {
            throw new CommandExecutionException();
        }
    }

    @Override
    public String execute() throws CommandExecutionException {
        return crackMessage(Configuration.instance.pathToRSACrackerJavaArchive, "RSACracker");
    }
}
