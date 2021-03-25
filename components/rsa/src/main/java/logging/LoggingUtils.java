package logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class LoggingUtils {

    public static PrintStream createSteamFromFile(File file) throws FileNotFoundException {
        return new PrintStream(file);
    }

    public static File createLoggingFile(String type, String algorithm) throws IOException {
        long currentUnixSeconds = System.currentTimeMillis() / 1000;
        return createLoggingFile(  type + "_" + algorithm + "_" + currentUnixSeconds + ".txt");
    }

    public static File createLoggingFile(String filename) throws IOException {
        final String directory = System.getProperty("user.dir") + System.getProperty("file.separator") + "log";

        File dir = new File(directory);
        if (!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(directory + System.getProperty("file.separator") + filename);
        file.createNewFile();
        return file;
    }
    public static void prepareLogger(Logger logger, String type, String algorithm) {
        if(logger.isEnabled()) {
            try {
                File loggingFile = LoggingUtils.createLoggingFile(type, algorithm);
                PrintStream stream = LoggingUtils.createSteamFromFile(loggingFile);
                logger.setOutput(stream);
            } catch (IOException e) {
                e.printStackTrace();
                logger.disable();
            }
        }
    }
}
