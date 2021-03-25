package commands;

import components.ComponentUtils;
import configuration.AlgorithmType;
import configuration.Configuration;

import java.io.File;
import java.io.InvalidObjectException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class EncryptMessageCommand implements ICommand{

    private String message;
    private AlgorithmType algorithm;
    private String keyfile;


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
            default -> throw new CommandExecutionException();
        }



    }

    private String callJarEncrypt(String jarPath, String className, String message, String keyfile) throws CommandExecutionException {
        try {
            var port = ComponentUtils.getPortFromJar(jarPath,className);
            var method = port.getClass().getDeclaredMethod("encrypt",String.class, File.class);
            return (String) method.invoke(port, message, new File(Configuration.instance.keyFilesDirectory + Configuration.instance.fileSeparator + keyfile));
        } catch (Exception exception) {
            Configuration.instance.logger.printError(exception.getStackTrace().toString());
            throw new CommandExecutionException();
        }
    }



    public class UnsupportedAlgorithmException extends Exception { }
}
