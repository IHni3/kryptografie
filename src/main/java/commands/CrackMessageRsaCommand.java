/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.Configuration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        return port.getClass().getDeclaredMethod("decrypt", String.class, File.class);
    }

    @Override
    protected Object onMethodInvoke(Object port, Method method) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(port, getMessage(), new File(Configuration.instance.keyFilesDirectory + "//" + getKeyfile()));
    }

    @Override
    protected String getJarPath() {
        return Configuration.instance.pathToRSACrackerJavaArchive;
    }

    @Override
    protected String getJarClass() {
        return "RsaCracker";
    }
}
