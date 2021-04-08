package configuration;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class LoggingHandler {
    Handler handler;
    Logger logger;

    public LoggingHandler(){
        logger = java.util.logging.Logger.getLogger("file");
    }

    public void newLogfile(String algorithmType, String direction){
        if (handler != null)
            logger.removeHandler(handler);
        try {
            handler = new FileHandler(String.format("log/%s_%s_%d.txt", direction, algorithmType, new Date().getTime()/1000));
            logger.addHandler(handler);
        } catch (IOException e){
            Configuration.instance.textAreaLogger.info("Problems creating logfile");
        }
    }

    public void disableLogging(){
        if (handler != null){
            logger.removeHandler(handler);
        }
    }

    public Logger getLogger(){
        return logger;
    }
}
