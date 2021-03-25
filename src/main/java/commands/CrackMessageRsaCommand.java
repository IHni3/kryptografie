package commands;

import components.ComponentUtils;
import configuration.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

public class CrackMessageRsaCommand extends CrackMessageCommand {
    private final String keyfile;

    public CrackMessageRsaCommand(String message, String keyfile) {
        super(message);
        this.keyfile = keyfile;
    }

    public String getKeyfile() {
        return keyfile;
    }

    @Override
    protected Method onConstructMethod(Object port) throws NoSuchMethodException {
        return port.getClass().getDeclaredMethod("decrypt", String.class, String.class);
    }

    @Override
    protected Object onMethodInvoke(Object port, Method method) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(port, getMessage(), getKeyfile());
    }

    @Override
    protected String getJarPath() {
        return Configuration.instance.pathToRSACrackerJavaArchive;
    }

    @Override
    protected String getJarClass() {
        return "RSACracker";
    }
}
