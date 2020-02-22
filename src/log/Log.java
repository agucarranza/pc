package log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    private static LogManager lgmngr = LogManager.getLogManager();
    public static Logger log = lgmngr.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Log() {
        try {
            FileHandler fh;
            fh = new FileHandler("./log.txt");
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


