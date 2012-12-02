package us.lordralex.modbot.irc;

import java.io.IOException;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import us.lordralex.modbot.config.Config;

/**
 * @version 1.0
 * @author Lord_Ralex
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

    /**
     * Gets the PircBotX instance in use
     *
     * @return The PircBotX driver in use
     */
    public PircBotX getDriver() {
        return driver;
    }

    /**
     * Returns the channel messages are being sent to
     *
     * @return Channel name
     */
    public String getChannel() {
        return CHANNEL;
    }
}
