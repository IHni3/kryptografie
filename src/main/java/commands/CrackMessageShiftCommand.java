/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CrackMessageShiftCommand extends CrackMessageCommand {

    public CrackMessageShiftCommand(String message) {
        super(message);
    }

    @Override
    protected Method onConstructMethod(Object port) throws NoSuchMethodException {
        return  port.getClass().getDeclaredMethod("decrypt", String.class);
    }

    @Override
    protected Object onMethodInvoke(Object port, Method method) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(port, getMessage());
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
