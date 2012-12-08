package com.lordralex.modbot;

import com.lordralex.modbot.config.Configuration;
import com.lordralex.modbot.mail.MailThread;
import com.lordralex.modbot.scanner.Scanner;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pircbotx.PircBotX;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Driver {

    private static final Logger logger = Logger.getLogger(Driver.class.getName());
    private static PircBotX ircbot;
    private static MailThread mailThread;
    private static Scanner scanner;

    @SuppressWarnings({"UnusedAssignment", "UseSpecificCatch"})
    public static void main(String[] args) throws IOException {
        try {
            Handler[] handlers = logger.getHandlers();
            for (Handler handler : handlers) {
                logger.removeHandler(handler);
            }
            handlers = null;
            System.gc();

            logger.addHandler(new ConsoleHandler());
            logger.addHandler(new FileHandler("logs"));

            logger.info("Starting up ModBot" + System.lineSeparator());

            logger.info("Setting up configuration");
            Configuration config = new Configuration("config");
            logger.info("Configuration loaded" + System.lineSeparator());

            logger.info("Connecting to IRC server");
            ircbot = new PircBotX();
            ircbot.setName(config.getString("nickname"));
            ircbot.connect(config.getString("irc-server"));
            ircbot.joinChannel(config.getString("channel"));
            logger.info("Connection to IRC server established" + System.lineSeparator());

            logger.info("Starting up mail connection");
            mailThread = new MailThread();
            logger.info("Connection to mail server established" + System.lineSeparator());

            logger.info("Setting up scanners");
            scanner = new Scanner();
            logger.info("Scanners set up" + System.lineSeparator());

            logger.info("Starting mail server threads");
            mailThread.start();
            logger.info("Mail server threads started");

            logger.info("All systems appear functional");
            
            
            Thread.sleep(60000);
            
            
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "An error occured that will prevent the bot from working", ex);
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public synchronized PircBotX getBot() {
        return ircbot;
    }
}
