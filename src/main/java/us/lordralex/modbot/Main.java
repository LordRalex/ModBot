package us.lordralex.modbot;

public class Main {

    private static Mail mail;
    private static IrcBot irc;
    private static IrcBotThread ircthread;

    public static void main(String[] args) throws Exception{
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
}
