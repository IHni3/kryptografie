/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import components.ComponentUtils;
import configuration.AlgorithmType;
import configuration.Configuration;

import java.io.File;

public class DecryptMessageCommand implements ICommand{

    private final String message;
    private final AlgorithmType algorithm;
    private final String keyfile;


    public DecryptMessageCommand(String message, AlgorithmType algorithm, String keyfile) {
        this.message = message;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }

    @Override
    public String execute() throws CommandExecutionException {
        switch (algorithm)
        {
            case RSA -> {
                return callJarEncrypt(Configuration.instance.pathToRSABaseJavaArchive, "Rsa", this.message, this.keyfile);
            }
            case SHIFT -> {
                return callJarEncrypt(Configuration.instance.pathToShiftBaseJavaArchive, "Shift", this.message, this.keyfile);
            }
            default -> throw new CommandExecutionException("Unsupported algorithm!");
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

            var method = port.getClass().getDeclaredMethod("decrypt",String.class, File.class);
            return (String) method.invoke(port, message, new File(Configuration.instance.keyFilesDirectory + Configuration.instance.fileSeparator + keyfile));
        } catch (Exception exception) {
            throw new CommandExecutionException("Calling method of component failed!", exception);
        }
    }

    public class UnsupportedAlgorithmException extends Exception { }
}
