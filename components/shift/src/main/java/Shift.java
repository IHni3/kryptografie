import logging.Logger;
import logging.LoggingUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;


public class Shift {
    // static instance
    private static final Shift instance = new Shift();

    //logger
    private Logger logger = new Logger();

    // port
    public Port port;

    int key;
    String plainMessage;
    String encryptedMessage;

    // private constructor
    private Shift() {
        port = new Port();
    }

    // static method getInstance
    public static Shift getInstance() {
        return instance;
    }

    // inner methods
    public String innerVersion() {
        return "Shift";
    }


    //Encrypt Message with Key from JSON File
    private String innerEncrypt(String plainMessage, File keyfile){
        LoggingUtils.prepareLogger(logger,"encrypt", "shiftbase");
        logger.printInfo("It works!!");

        this.key = readJsonFile(keyfile);

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i< plainMessage.length(); i++){
            char character = (char) (plainMessage.codePointAt(i) + key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    //Decrypt Message with Key from JSON File
    private String innerDecrypt(String encryptedMessage, File keyfile){
        LoggingUtils.prepareLogger(logger,"decrypt", "shiftbase");
        logger.printInfo("It works!!");

        this.key = readJsonFile(keyfile);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            char character = (char) (encryptedMessage.codePointAt(i) - key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }


    //Read JSON File into Integer Key
    private int readJsonFile(File keyfile){
        int key;
        try {
            FileReader reader = new FileReader(keyfile);
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            key = Integer.parseInt(jsonObject.get("key").toString());

        } catch(Exception ex){
            throw new RuntimeException(ex);
        }

        return key;
    }

    private void innerEnabledDebuggingMode() {
        logger.enable();
    }



    // inner class port
    public class Port implements IShift {
        @Override
        public String version() {
            return innerVersion();
        }

        @Override
        public String decrypt(String plainMessage, File keyfile) {
            return innerDecrypt(plainMessage, keyfile);
        }

        @Override
        public String encrypt(String encryptedMessage, File keyfile) {
            return innerEncrypt(encryptedMessage, keyfile);
        }

        @Override
        public void enableDebuggingMode() {
            innerEnabledDebuggingMode();
        }
    }
}