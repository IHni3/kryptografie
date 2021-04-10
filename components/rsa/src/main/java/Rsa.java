import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Base64;
import logging.Logger;
import logging.LoggingUtils;

import com.google.gson.Gson;

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
        logger.printInfo("Performing encryption via m ^ e mod n");
        byte[] encryptedMessage = cipher.encrypt(plain, key);
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    public String decryptRSA(String encrypted, Key key){
        Cipher cipher = new Cipher();
        byte[] encryptedMessage = Base64.getDecoder().decode(encrypted);
        logger.printInfo("Performing decryption via m ^ d mod n");
        return cipher.decrypt(encryptedMessage, key);
    }

    public static Rsa getInstance() {
        return instance;
    }

    public class Port {
        public String version() {
            return null;
        }

        public String encrypt(String plain, File keyfile) throws IOException {
            LoggingUtils.prepareLogger(logger, "encrypt", "rsa");

            var parsed = parseKeyfile(keyfile);

            Key key = new Key(parsed.n, parsed.e);
            logger.printInfo("Parsing done public key: " + key.getE());

            logger.printInfo("Encrypting message \"" + plain + "\"");
            return encryptRSA(plain, key);
        }

        public String decrypt(String cipher,File keyfile) throws IOException {
            LoggingUtils.prepareLogger(logger, "decrypt", "rsa");

            var parsed = parseKeyfile(keyfile);

            Key key = new Key(parsed.n, parsed.d);

            logger.printInfo("Parsed keyfile private key: " + key.getE());

            logger.printInfo("Decrypting message \"" + cipher + "\"");
            return decryptRSA(cipher, key);
        }

        private Keyfile parseKeyfile(File keyfile) throws IOException {
            Gson gson = new Gson();
            Reader reader = null;
            try {
                logger.printInfo("Loading keyfile" + keyfile);
                reader = Files.newBufferedReader(keyfile.toPath());
            } catch (Exception e) { }
            logger.printInfo("Parsing keyfile");
            Keyfile parsed = gson.fromJson(reader, Keyfile.class);

            if (parsed == null) {
                throw new IOException();
            }

            return parsed;
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
