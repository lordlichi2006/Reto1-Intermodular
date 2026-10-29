package com.reto1.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads the application settings from /config.properties (src/main/resources).
 * If the file or a key is missing, the default value passed to {@link #get} is used.
 */
public final class AppConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = AppConfig.class.getResourceAsStream("/config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            System.err.println("config.properties could not be read: " + e.getMessage());
        }
    }

    private AppConfig() {
    }

    /**
     * @param key          name of the setting, e.g. "mongo.uri"
     * @param defaultValue value returned when the key does not exist
     * @return the configured value or the default one
     */
    public static String get(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }
}
