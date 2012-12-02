package us.lordralex.modbot;

import java.util.logging.Logger;
import us.lordralex.modbot.irc.IrcBot;
import us.lordralex.modbot.irc.IrcBotThread;
import us.lordralex.modbot.mail.Mail;

public class Main {

    private static Mail mail;
    private static IrcBot irc;
    private static IrcBotThread ircthread;
    private static final Logger logger;

    static {
        logger = Logger.getLogger("ModBot");
    }

    public static void main(String[] args) throws Exception {
        mail = new Mail();
        irc = new IrcBot();
        ircthread = new IrcBotThread();
        ircthread.start();
        mail.start();
    }

    public static Mail getMail() {
        return mail;
    }

    public static IrcBot getIrc() {
        return irc;
    }

    public static Thread getIrcThread() {
        return ircthread;
    }

    public static Logger getLogger() {
        return logger;
    }
}
