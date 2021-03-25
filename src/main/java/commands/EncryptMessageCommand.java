package commands;

import components.ComponentUtils;
import configuration.AlgorithmType;
import configuration.Configuration;

import java.io.File;

public class EncryptMessageCommand implements ICommand{

    private final String message;
    private final AlgorithmType algorithm;
    private final String keyfile;


    public EncryptMessageCommand(String message, AlgorithmType algorithm, String keyfile) {
        this.message = message;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }

    @Override
    public String execute() throws CommandExecutionException {
        switch (algorithm)
        {
            case RSABASE -> {
                return callJarEncrypt(Configuration.instance.pathToRSABaseJavaArchive, "RSABase", this.message, this.keyfile);
            }
            case SHIFTBASE -> {
                return callJarEncrypt(Configuration.instance.pathToShiftBaseJavaArchive, "ShiftBase", this.message, this.keyfile);
            }
            default -> throw new CommandExecutionException("Unsupported Algorithm!");
        }
    }

    public String getMessage() {
        return message;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public String getKeyfile() {
        return keyfile;
    }

    private String callJarEncrypt(String jarPath, String className, String message, String keyfile) throws CommandExecutionException {
        try {
            var port = ComponentUtils.getPortFromJar(jarPath,className);

            if(Configuration.instance.debugModeEnabled) {
                var loggingEnableMethod = port.getClass().getDeclaredMethod("enableDebuggingMode");
                loggingEnableMethod.invoke(port);
            }

            var method = port.getClass().getDeclaredMethod("encrypt",String.class, File.class);
            return (String) method.invoke(port, message, new File(Configuration.instance.keyFilesDirectory + Configuration.instance.fileSeparator + keyfile));
        } catch (Exception exception) {
            throw new CommandExecutionException("calling method of component failed!", exception);
        }
    }

    public class UnsupportedAlgorithmException extends Exception { }
}
