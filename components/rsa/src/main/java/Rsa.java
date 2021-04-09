import java.io.File;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Base64;
import com.google.gson.Gson;
import logging.Logger;
import logging.LoggingUtils;

public class Rsa {
    private static Rsa instance = new Rsa();
    public Port port;

    //logger
    private final Logger logger = new Logger();

    public Rsa(){
        port = new Port();
    }

    public String encryptRSA(String plain, Key key){
        Cipher cipher = new Cipher();
        byte[] encryptedMessage = cipher.encrypt(plain, key);
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    public String decryptRSA(String encrypted, Key key){
        Cipher cipher = new Cipher();
        byte[] encryptedMessage = Base64.getDecoder().decode(encrypted);
        return cipher.decrypt(encryptedMessage, key);
    }

    public static Rsa getInstance() {
        return instance;
    }

    public class Port {
        public String version() {
            return null;
        }

        public String encrypt(File keyfile, String plain) {

            LoggingUtils.prepareLogger(logger,"decrypt", "rsa");
            logger.printInfo("It works!!");

            Gson gson = new Gson();
            Reader reader = null;
            try {
                reader = Files.newBufferedReader(keyfile.toPath());
            } catch (Exception e) { }
            Keyfile parsed = gson.fromJson(reader, Keyfile.class);

            Key key = new Key(parsed.n, parsed.e);

            return encryptRSA(plain, key);
        }

        public String decrypt(File keyfile, String cipher) {
            Gson gson = new Gson();
            Reader reader = null;
            try {
                reader = Files.newBufferedReader(keyfile.toPath());
            } catch (Exception e) { }
            Keyfile parsed = gson.fromJson(reader, Keyfile.class);

            Key key = new Key(parsed.n, parsed.d);

            return decryptRSA(cipher, key);
        }

        public void enableDebuggingMode() {
            logger.enable();
        }
    }

    public class Keyfile{
        BigInteger e;
        BigInteger d;
        BigInteger n;

        public Keyfile(){}

        public Keyfile(BigInteger e, BigInteger d, BigInteger n){
            this.e = e;
            this.d = d;
            this.n = n;
        }

    }
}
