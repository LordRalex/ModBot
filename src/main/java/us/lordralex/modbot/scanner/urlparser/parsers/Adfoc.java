/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.lordralex.modbot.scanner.urlparser.parsers;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import us.lordralex.modbot.scanner.urlparser.URLParser;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Adfoc implements URLParser{
    
    @Override
    public String getLink(List<String> pageSrc) {
        DataInputStream input = null;
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
            List<String> lines = new ArrayList<>();
            try {
                String line;
                while ((line = input.readLine()) != null) {
                    lines.add(line.trim());
                }
            } catch (EOFException e) {
                input.close();
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
            Logger.getLogger(Adfly.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(Adfly.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
}
