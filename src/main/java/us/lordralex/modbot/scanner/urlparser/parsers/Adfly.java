/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.lordralex.modbot.scanner.urlparser.parsers;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import us.lordralex.modbot.Main;
import us.lordralex.modbot.scanner.urlparser.URLParser;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Adfly implements URLParser {

    @Override
    public String getLink(List<String> pageSrc) {
        DataInputStream input = null;
        BufferedReader reader = null;
        try {
            List<String> b = null;
            String end = null;
            for (String string : b) {
                string = string.trim();
                if (string.startsWith("var url")) {
                    end = string.replace("var url =", "");
                    end = end.replace("\'", "");
                    end = end.replace(";", "");
                    end = end.trim();
                    if (!end.startsWith("https://adf.ly/")) {
                        end = "https://adf.ly/" + end;
                    }
                    break;
                }
            }

            if (end == null) {
                return null;
            }

            URL newD = new URL(end);
            HttpURLConnection newC = (HttpURLConnection) newD.openConnection();
            input = new DataInputStream(newC.getInputStream());
            reader = new BufferedReader(new InputStreamReader(input));
            List<String> lines = new ArrayList<>();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line.trim());
                }
            } catch (EOFException e) {
                input.close();
                reader.close();
            }

            String result = newD.toExternalForm();

            for (String line : lines) {
                System.out.println(line);
                if (line.startsWith("<META")) {
                    result = line.split("URL=")[1].replace("\"", "").replace(">", "");
                }
            }
            return result;
        } catch (IOException ex) {
            Main.getLogger().log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Main.getLogger().log(Level.SEVERE, null, ex);
            }
            try {
                reader.close();
            } catch (IOException ex) {
                Main.getLogger().log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
