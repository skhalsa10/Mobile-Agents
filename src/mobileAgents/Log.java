package mobileAgents;

import mobileAgents.messages.Message;
import mobileAgents.messages.MessageGUIEnd;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * this class is essentially just a wrapper to the Java logger.
 * It  will accept both Messages and strings that can be logged
 */
public class Log {
    private Logger logger;
    private FileHandler fh;

    public Log() throws IOException {
        //the logger accepts messages
        logger = Logger.getLogger("MobileAgents.log");
        //filehandler is the location it goes
        fh = new FileHandler("MobileAgents.log");
        logger.addHandler(fh);
        //the formatter changes the format before it gets sent to the file
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        //the following turns off logging into the console.
        //logger.setUseParentHandlers(false);
    }

    /**
     * this will take a message and log the read message from it.
     * @param m
     */
    public void logMessage(Message m){
        logger.info(m.readMessage());
    }

    /**
     * this will log the string passed to it
     * @param s
     */
    public void logString(String s){
        logger.info(s);
    }
    

}
