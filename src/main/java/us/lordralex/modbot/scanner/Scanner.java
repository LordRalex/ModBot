package us.lordralex.modbot.scanner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.pircbotx.Colors;
import us.lordralex.modbot.DownloadFile;
import us.lordralex.modbot.Main;
import us.lordralex.modbot.irc.IrcBot;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Scanner {

    private final Map<String, DownloadFile> fileMapping = new ConcurrentHashMap<>();

    public Scanner() {
    }

    public void addNewFile(String thread, String name, String link) {
        DownloadFile file = new DownloadFile(thread, name);
        fileMapping.put(name, file);
    }

    public void handleNewScan(String scanName, String scanResult) {
        DownloadFile file = fileMapping.remove(scanName);
        if (file == null) {
            return;
        }
        if (!scanResult.equalsIgnoreCase("0")) {
            IrcBot bot = Main.getIrc();
            bot.getDriver().sendMessage(bot.getChannel(), Colors.BOLD + Colors.RED + "http://minecraftforum.net/topic/" + file.thread_id + "- returned with a result of " + scanResult + " positives for " + file.fileName);
        }
    }
}
