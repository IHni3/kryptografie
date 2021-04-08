package commands;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.Callable;

public class CrackRSATask implements Callable<String> {
    String keyfile;
    String message;

    public CrackRSATask(String keyfile, String message) {
        this.keyfile = keyfile;
        this.message = message;
    }

    @Override
    public String call() throws Exception {
        File keyfileFile = new File(System.getProperty("user.dir") + "/keyfiles/" + keyfile);
        String jarName = "rsa_cracker.jar";
        try {
            var port = Loader.getPort(System.getProperty("user.dir") + "/resources/"+ jarName +".jar", "cracker");
            var method = port.getClass().getDeclaredMethod("encrypt", BigInteger.class, BigInteger.class, String.class);
            return method.invoke(port, message, keyfileFile).toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
