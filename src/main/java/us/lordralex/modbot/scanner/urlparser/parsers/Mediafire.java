/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.lordralex.modbot.scanner.urlparser.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import us.lordralex.modbot.scanner.urlparser.URLParser;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Mediafire implements URLParser {

    @Override
    public String getLink(List<String> pageSrc) {
        List<String> d = new ArrayList<>();
        for (String part : pageSrc) {
            String[] e = part.split(",");
            d.addAll(Arrays.asList(e));
        }
        for (int i = 0; i < d.size(); i++) {
            d.add(i, d.remove(i).trim());
            if (d.get(i).equalsIgnoreCase("")) {
                d.remove(i);
                i--;
            }
        }
        for (int i = 0; i < d.size(); i++) {
            String test = d.get(i);
            if (test.startsWith("kNO = ")) {
                test = test.replace("kNO = ", "").replace("\"", "").trim();
                return test;
            }
        }
        return null;
    }
}
