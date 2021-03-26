/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package configuration;

import logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Configuration {
    instance;

    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String commonPathToJavaArchive = userDirectory + fileSeparator + "components" + fileSeparator;

    // Keyfile Directory
    public final String keyFilesDirectory = userDirectory + fileSeparator + "keyfiles";

    // Shift
    public final String pathToShiftBaseJavaArchive = commonPathToJavaArchive + "shift" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "shift.jar";

    // ShiftCracker
    public final String pathToShiftCrackerJavaArchive = commonPathToJavaArchive + "shift_cracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "shift_cracker.jar";

    // RSABase
    public final String pathToRSABaseJavaArchive = commonPathToJavaArchive + "rsa" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "rsa.jar";

    // RSACracker
    public final String pathToRSACrackerJavaArchive = commonPathToJavaArchive + "rsa_" +
            "cracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "rsa_cracker.jar";



    // Database
    public final String databaseDirectory = userDirectory + fileSeparator + "database" + fileSeparator;
    public final String databaseFile = databaseDirectory + "cryptoDB.db";
    public final String dbDriverName = "jdbc:hsqldb:";
    public final String dbUsername = "sa";
    public final String dbPassword = "";


    // Log-Directory
    public final String logsDirectory = userDirectory + fileSeparator + "log";
    public boolean debugModeEnabled = false;

    private Logger logger = new Logger(System.out);

    public void setLogger(Logger logger){
        this.logger = logger;
    }
    public Logger getLogger(){
        return logger;
    }


    public void enableLogging(){

    }

    public void disableLogging(){

    }

    //Test Field Logger
    public java.util.logging.Logger textAreaLogger = java.util.logging.Logger.getLogger("test.test");

    // Intruded Channels
    public Map<String, String> intrudedChannels = new HashMap<>();


}
