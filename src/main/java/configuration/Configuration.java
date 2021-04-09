/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Configuration {
    instance;

    public final String jarPath = "jars/";

    // Paths
    public final String keyFiles = "keyfiles/";
    public final String shiftJar = jarPath + "shift.jar";
    public final String shiftCrackerJar = jarPath + "shift_cracker.jar";
    public final String rsaJar = jarPath + "rsa.jar";
    public final String rsaCrackerJar = jarPath + "rsa_cracker.jar";

    // Database
    public final String databaseDirectory = "database/";
    public final String databaseFile = databaseDirectory + "cryptoDB.db";
    public final String dbDriverName = "jdbc:hsqldb:";
    public final String dbUsername = "sa";
    public final String dbPassword = "";

    //JDK Path
    public final String jdkPath = "/home/liam/.local/java/jdk-15.0.2/bin/";
    // Log-Directory
    public final String logDir =  "log";

    //Text Field Logger
    public final java.util.logging.Logger textAreaLogger = java.util.logging.Logger.getLogger("textarea");

    // Intruded Channels
    public final Map<String, String> intrudedChannels = new HashMap<>();

    //File Logger
    public final LoggingHandler loggingHandler = new LoggingHandler();


}
