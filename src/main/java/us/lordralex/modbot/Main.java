package us.lordralex.modbot;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import us.lordralex.modbot.irc.IrcBot;
import us.lordralex.modbot.irc.IrcBotThread;
import us.lordralex.modbot.mail.Mail;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Main {

    private static Mail mail;
    private static IrcBot irc;
    private static IrcBotThread ircthread;
    private static final Logger logger;

    static {
        logger = Logger.getLogger("ModBot");

        //removes any existing handlers from the logger
        Handler[] list = logger.getHandlers();
        for (Handler h : list) {
            logger.removeHandler(h);
        }

        //add a new console handler logging only config+ to console
        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.WARNING);
        logger.addHandler(console);

        //add a new filehandler to log everything to a file (max file size: 20MB, up to 20 files
        try {
            FileHandler file = new FileHandler("logs", 1024 * 1024 * 20, 20, true);
            file.setLevel(Level.ALL);
            logger.addHandler(file);
        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Main method used to start the bot. This handles all the declarations and
     * creations.
     *
     * @param args Arguments passed in on start
     * @throws Exception Any uncaught exception on starting may be thrown and
     * cause stop of program
     */
    public static void main(String[] args) throws Exception {
        mail = new Mail();
        irc = new IrcBot();
        ircthread = new IrcBotThread();
        ircthread.start();
        mail.start();
    }

    /**
     * Returns the mail instance in use
     *
     * @return Mail instance used
     */
    public static Mail getMail() {
        return mail;
    }

    /**
     * Returns the irc instance in use
     *
     * @return Irc instance used
     */
    public static IrcBot getIrc() {
        return irc;
    }

    /**
     * Returns the irc thread instance in use
     *
     * @return IrcThread instance in use
     */
    public static Thread getIrcThread() {
        return ircthread;
    }

    /**
     * Returns the Logger
     *
     * @return Logger in use
     */
    public static Logger getLogger() {
        return logger;
    }
}
