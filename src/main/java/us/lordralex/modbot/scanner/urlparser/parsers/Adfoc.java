package us.lordralex.modbot.scanner.urlparser.parsers;

import java.util.List;
import us.lordralex.modbot.scanner.urlparser.URLParser;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Adfoc implements URLParser {

    @Override
    public String getLink(List<String> pageSrc) {
        List<String> b = null;
        String end = null;
        for (String string : b) {
            string = string.trim();
            if (string.startsWith("Lbjs.TargetUrl")) {
                end = string.replace("Lbjs.TargetUrl = ", "");
                end = end.replace("\'", "");
                end = end.replace(";", "");
                end = end.trim();
            }
        }

        return end;
    }
}
