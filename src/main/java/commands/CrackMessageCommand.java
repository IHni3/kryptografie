package commands;

import components.ComponentUtils;
import configuration.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

public abstract class CrackMessageCommand implements ICommand{

    private final String message;
    private static final int MAX_EXECUTION_TIME = 30; //seconds

    public CrackMessageCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String execute() throws CommandExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task());
        String decryptedMessage = "";
        try {
            decryptedMessage = future.get(MAX_EXECUTION_TIME, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (Exception e) {
            throw new CommandExecutionException("Task execution failed!", e);
        }

        executor.shutdownNow();

        return decryptedMessage;
    }

    private String crackMessage() throws CommandExecutionException {
        try {
            var port = ComponentUtils.getPortFromJar(getJarPath(), getJarClass());
            var method = onConstructMethod(port);
            var decryptedMessage = String.valueOf(onMethodInvoke(port,method));
            return decryptedMessage;
        } catch (Exception exception) {
            throw new CommandExecutionException("Calling method of component failed!", exception);
        }
    }


    class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            return crackMessage();
        }
    }

    protected abstract Method onConstructMethod(Object port) throws NoSuchMethodException;
    protected abstract Object onMethodInvoke(Object port, Method method) throws InvocationTargetException, IllegalAccessException;
    protected abstract String getJarPath();
    protected abstract String getJarClass();
}
