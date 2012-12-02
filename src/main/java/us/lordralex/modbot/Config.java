package us.lordralex.modbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joshua
 */
public class Config {

    private final Map<String, String> mapping;
    private static final Map<String, Map<String, String>> allLoaded = new ConcurrentHashMap<>();

    public Config(File file) {
        mapping = new ConcurrentHashMap<>();
        if (allLoaded.containsKey(file.getName().toLowerCase())) {
            mapping.putAll(allLoaded.get(file.getName().toLowerCase()));
        } else if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    String key = line.split(":")[0];
                    String value;
                    try {
                        value = line.split(":")[1];
                    } catch (IndexOutOfBoundsException e) {
                        value = "";
                    }
                    key = key.trim();
                    value = value.trim();
                    mapping.put(key.toLowerCase(), value);
                }
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        allLoaded.put(file.getName().toLowerCase(), mapping);
    }

    public Config(String file) {
        this(new File(file));
    }

    public String getString(String key) {
        return mapping.get(key.toLowerCase());
    }

    public static String getStringFromFile(String file, String key) {
        Map<String, String> map = allLoaded.get(file.toLowerCase());
        if (map == null) {
            return null;
        } else {
            return map.get(key);
        }
    }
}
