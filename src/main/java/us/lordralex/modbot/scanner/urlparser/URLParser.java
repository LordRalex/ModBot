package us.lordralex.modbot.scanner.urlparser;

import java.util.List;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public interface URLParser {

    /**
     * Parses a page's source and returns some desired String. This is to be
     * used to parse any page and return some String as to what the class was
     * getting.
     *
     * @param pageSrc The page source
     * @return Result of the parse
     */
    public abstract String getLink(List<String> pageSrc);
}
