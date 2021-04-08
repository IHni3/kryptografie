package gui;

import configuration.Configuration;
import database.HSQLDB;

public class Launcher {
    public static void main(String[] args) {
        init();
        GUI gui = new GUI();
        gui.main(args);
    }

    private static void init(){
        HSQLDB.instance.setupConnection();
        Configuration.instance.textAreaLogger.setUseParentHandlers(false);
    }
}
