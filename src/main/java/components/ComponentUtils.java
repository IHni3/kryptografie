package components;

import commands.EncryptMessageCommand;
import configuration.Configuration;

import java.io.File;
import java.io.InvalidObjectException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ComponentUtils {

    public static URL[] generateUrls(String path) throws MalformedURLException {
        return new URL[]{new File(path).toURI().toURL()};
    }
    public static  Class loadClass(URL[] urls,String className) throws ClassNotFoundException {
        URLClassLoader urlClassLoader = new URLClassLoader(urls, EncryptMessageCommand.class.getClassLoader());
        return Class.forName(className, true, urlClassLoader);
    }
    public static  Object getInstanceOfClass(Class clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return clazz.getMethod("getInstance").invoke(null);
    }
    public static  Object getPortOfClass(Class clazz, Object instance) throws NoSuchFieldException, IllegalAccessException {
        return clazz.getDeclaredField("port").get(instance);
    }
    public static  Object getPortFromJar(String path, String className) throws InvalidObjectException {
        try {
            var urls = generateUrls(path);
            var clazz = loadClass(urls, className);
            var instance = getInstanceOfClass(clazz);
            return getPortOfClass(clazz, instance);
        } catch (Exception exception) {
            Configuration.instance.logger.printCritical(exception.getStackTrace().toString());
            throw new InvalidObjectException("getting port from jar failed!");
        }
    }
}
