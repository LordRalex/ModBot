package us.lordralex.modbot;

import java.io.IOException;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;

/**
 *
 * @author Joshua
 */
public class IrcBot extends ListenerAdapter {

    private PircBotX driver;
    public static final String CHANNEL = "#***************";
    private static final String NAME = "***********";
    private static final String NETWORK = "irc.esper.net";

    public IrcBot() throws IOException, IrcException {
        driver = new PircBotX();
        driver.setName(NAME);
        driver.setLogin(NAME);
        driver.setVersion("0.1");
        driver.connect(NETWORK);
        driver.joinChannel(CHANNEL);
    }

    public PircBotX getDriver() {
        return driver;
    }
}
