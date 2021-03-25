package configuration;

import logging.Logger;

public enum Configuration {
    instance;

    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String commonPathToJavaArchive = userDirectory + fileSeparator + "components" + fileSeparator;
    public final String lineSeparator = System.getProperty("line.separator");

    // Keyfile Directory
    public final String keyFilesDirectory = userDirectory + fileSeparator + "keyfiles";

    // ShiftBase
    public final String pathToShiftBaseJavaArchive = commonPathToJavaArchive + "shiftBase" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "ShiftBase.jar";

    // ShiftCracker
    public final String pathToShiftCrackerJavaArchive = commonPathToJavaArchive + "shiftCracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "ShiftCracker.jar";

    // RSABase
    public final String pathToRSABaseJavaArchive = commonPathToJavaArchive + "rsaBase" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "RSABase.jar";

    // RSACracker
    public final String pathToRSACrackerJavaArchive = commonPathToJavaArchive + "rsaCracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "RSACracker.jar";



    // Database
    public final String databaseDirectory = userDirectory + fileSeparator + "database" + fileSeparator;
    public final String databaseFile = databaseDirectory + "cryptoDB.db";
    public final String dbDriverName = "jdbc:hsqldb:";
    public final String dbUsername = "sa";
    public final String dbPassword = "";


    // Log-Directory
    public final String logsDirectory = userDirectory + fileSeparator + "log";

    public final Logger logger = new Logger(System.out);


    public void enableLogging(){

    }

    public void disableLogging(){

    }
}
