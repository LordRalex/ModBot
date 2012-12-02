package us.lordralex.modbot.irc;

import java.io.IOException;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import us.lordralex.modbot.config.Config;

/**
 *
 * @author Joshua
 */
public class IrcBot extends ListenerAdapter {

    private PircBotX driver;
    private final String CHANNEL;
    private final String NAME;
    private final String NETWORK;

    public IrcBot() throws IOException, IrcException {
        String tempV;

        CHANNEL = ((tempV = Config.getStringFromFile("config", "channel")) == null) ? "" : tempV;
        NAME = ((tempV = Config.getStringFromFile("config", "nickname")) == null) ? "" : tempV;
        NETWORK = ((tempV = Config.getStringFromFile("config", "network")) == null) ? "" : tempV;

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

    public String getChannel() {
        return CHANNEL;
    }
}
