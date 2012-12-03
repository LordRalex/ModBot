/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.lordralex.modbot.scanner.urlparser;

import java.util.List;

/**
 *
 * @author Joshua
 */
public interface URLParser {

    public abstract String getLink(List<String> pageSrc);
}
