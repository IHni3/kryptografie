import logging.Logger;
import logging.LoggingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Scanner;

public class Rsa {
    private static Rsa instance = new Rsa();
    public Port port;
    private Key key;

    //logger
    private Logger logger = new Logger();

    private Rsa() {
        port = new Port();
    }

    public static Rsa getInstance() {
        return instance;
    }

    private String encryptMessage(String plainMessage, File publicKeyfile) throws FileNotFoundException {
        LoggingUtils.prepareLogger(logger,"encrypt", "rsa");
        logger.printInfo("It works!!");


        readPublicKeyFile(publicKeyfile);

        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        byte[] encrypted = crypt(new BigInteger(bytes), key).toByteArray();
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decryptMessage(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
        LoggingUtils.prepareLogger(logger,"decrypt", "rsa");
        logger.printInfo("It works!!");


        readPrivateKeyFile(privateKeyfile);

        byte[] cipher = Base64.getDecoder().decode(encryptedMessage);
        byte[] msg = crypt(new BigInteger(cipher), key).toByteArray();
        return new String(msg);
    }

    private BigInteger crypt(BigInteger message, Key key) {
        return message.modPow(key.getE(), key.getN());
    }

    private void readPrivateKeyFile(File keyfile) throws FileNotFoundException {
        readKeyFromFile(keyfile, "d");
    }

    private void readPublicKeyFile(File keyfile) throws FileNotFoundException {
        readKeyFromFile(keyfile);
    }

    private void readKeyFromFile(File keyfile) throws FileNotFoundException {
        readKeyFromFile(keyfile, "e");
    }

    private void readKeyFromFile(File keyfile, String eReplacement) throws FileNotFoundException {
        BigInteger n = null;
        BigInteger e = null;

        Scanner scanner = new Scanner(keyfile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("\"n\":")) {
                n = getParameter(line);
            } else if (line.contains("\"" + eReplacement + "\":")) {
                e = getParameter(line);
            }
        }

        this.key = new Key(n, e);
    }

    private BigInteger getParameter(String input) {
        String[] lineParts = input.split(":");
        String line = lineParts[1];
        line = line.replace(",", "").trim();
        return new BigInteger(line);
    }

    private void innerEnabledDebuggingMode() {
        logger.enable();
    }

    public class Port implements IRsa {
        @Override
        public String version() {
            return null;
        }

        @Override
        public String encrypt(String plainMessage, File publicKeyfile) throws FileNotFoundException {
            return encryptMessage(plainMessage, publicKeyfile);
        }

        @Override
        public String decrypt(String encryptedMessage, File privateKeyfile) throws FileNotFoundException {
            return decryptMessage(encryptedMessage, privateKeyfile);
        }

        @Override
        public void enableDebuggingMode() {
            innerEnabledDebuggingMode();
        }
    }
}