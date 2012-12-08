package com.lordralex.modbot.config;

import com.lordralex.modbot.Driver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * @version 1.0
 * @author Lord_Ralex
 */
public class Configuration {

    private static Map<String, Map<String, String>> settings = new ConcurrentHashMap<>();
    private final String name;

    public Configuration(String file) {
        name = file;
        if (settings.containsKey(name)) {
            Driver.getLogger().warning("This config (" + name + ") was already loaded before. Not going to re-load it again.");
            return;
        }
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
                String line;
                Map<String, String> read = new HashMap<>();
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split(":", 2);
                    read.put(split[0].trim(), split[1].trim());
                }
                if (!read.isEmpty()) {
                    settings.put(name, read);
                }
            }
        } catch (IOException e) {
            Driver.getLogger().log(Level.SEVERE, "Failed to load file", e);
        }
    }

    public static String getString(String file, String key) {
        return settings.get(file).get(key);
    }

    public String getString(String key) {
        return settings.get(name).get(key);
    }
}
