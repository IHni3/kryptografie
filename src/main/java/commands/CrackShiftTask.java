package commands;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class CrackShiftTask implements Callable<String> {
    String message;

    public CrackShiftTask(String message) {
        this.message = message;
    }

    @Override
    public String call() throws Exception {
        String jarName = "shift_cracker.jar";
        try {
            var port = Loader.getPort(System.getProperty("user.dir") + "/resources/"+ jarName +".jar", "cracker");
            var method = port.getClass().getDeclaredMethod("encrypt", BigInteger.class, BigInteger.class, String.class);
            return method.invoke(port, message).toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
