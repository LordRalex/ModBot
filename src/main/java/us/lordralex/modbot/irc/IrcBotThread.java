package us.lordralex.modbot.irc;

import java.util.List;
import org.pircbotx.PircBotX;
import us.lordralex.modbot.Main;

/**
 *
 * @author Joshua
 */
public class IrcBotThread extends Thread {

    public IrcBotThread() {
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
                driver.sendMessage(Main.getIrc().getChannel(), thread);
            }
        }
    }
}
