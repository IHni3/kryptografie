package commands;

import configuration.Configuration;

import java.io.File;
import java.io.IOException;
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
        File keyfileFile = new File(Configuration.instance.keyFiles + keyfile);
        String jarName = "rsa_cracker.jar";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = Loader.getPort(Configuration.instance.jarPath + jarName +".jar", "cracker");
            var method = port.getClass().getDeclaredMethod("decrypt", String.class, File.class);
            return method.invoke(port, message, keyfileFile).toString();
        } catch (IOException e){
            Configuration.instance.textAreaLogger.info("Keyfile could not be Processed");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
