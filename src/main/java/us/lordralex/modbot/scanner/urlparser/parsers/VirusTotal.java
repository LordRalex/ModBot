/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.lordralex.modbot.scanner.urlparser.parsers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import us.lordralex.modbot.Main;
import us.lordralex.modbot.scanner.urlparser.URLParser;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class VirusTotal implements URLParser {

    DocumentBuilderFactory dbf;

    public VirusTotal() {
        dbf = DocumentBuilderFactory.newInstance();
    }

    @Override
    public String getLink(List<String> pageSrc) {
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (String line : pageSrc) {
                baos.write(line.getBytes());
            }
            byte[] bytes = baos.toByteArray();
            InputStream reader = new ByteArrayInputStream(bytes);
            Document doc = db.parse(reader);

            List<Node> l = new ArrayList<>();
            NodeList a = doc.getElementsByTagName("_response_");
            for (int i = 0; i < a.getLength(); i++) {
                l.add(a.item(i));
            }
            int positive = 0;
            for (Node node : l) {
                String result = node.getTextContent();
                if (!result.equalsIgnoreCase("-")) {
                    positive++;
                }
            }
            return Integer.toString(positive);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Main.getLogger().log(Level.SEVERE, null, ex);
            return "0";
        }
    }
}
