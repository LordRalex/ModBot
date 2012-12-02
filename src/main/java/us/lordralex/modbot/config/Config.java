package us.lordralex.modbot.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Config {

    private final Map<String, String> mapping;
    private static final Map<String, Map<String, String>> allLoaded = new ConcurrentHashMap<>();
    private final String fileName;

    /**
     * Creates a new config loader. This reads the file if it exists, and adds
     * it to the collection.
     *
     * @param file The File to load
     */
    public Config(File file) {
        mapping = new ConcurrentHashMap<>();
        fileName = file.getName().toLowerCase();
        if (allLoaded.containsKey(fileName)) {
            mapping.putAll(allLoaded.get(fileName));
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
        allLoaded.put(fileName, mapping);
    }

    /**
     * Creates a new config loader. This reads the file if it exists, and adds
     * it to the collection.
     *
     * @param file The File to load
     */
    public Config(String file) {
        this(new File(file));
    }

    /**
     * Gets a value from a given key. This uses the config from this
     *
     * @param key Key to get
     * @return The value of the key, or null if the value does not exist
     */
    public String getString(String key) {
        return getStringFromFile(fileName, key);
    }

    /**
     * Gets a value from a specific config. If the config does not exist, was
     * not loaded, or the key does not exist in the config, this returns null
     *
     * @param file The name of the config
     * @param key The key to get
     * @return The value of the key, or null if the config/key does not exist
     */
    public static String getStringFromFile(String file, String key) {
        Map<String, String> map = allLoaded.get(file.toLowerCase());
        if (map == null) {
            return null;
        } else {
            return map.get(key);
        }
    }
}
