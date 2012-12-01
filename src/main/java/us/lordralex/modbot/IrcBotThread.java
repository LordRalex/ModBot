package us.lordralex.modbot;

import java.util.List;
import org.pircbotx.PircBotX;

/**
 *
 * @author Joshua
 */
public class IrcBotThread extends Thread {

    public IrcBotThread ()
    {
        this.setName("Irc_Thread");
    }

    @Override
    public void run() {
        PircBotX driver = Main.getIrc().getDriver();
        while (driver.isConnected()) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace(System.err);
                }
            }
            List<String> threads = Main.getMail().getThreads();
            for (String thread : threads) {
                driver.sendMessage(IrcBot.CHANNEL, thread);
            }
        }
    }
}
