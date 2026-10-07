package com.aib.config;

import com.aib.constants.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Singleton configuration reader supporting property overrides via System properties.
 */
public final class ConfigReader {

    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
            properties.load(fis);
            log.info("Loaded configuration properties from: {}", FrameworkConstants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            log.warn("Failed to load config.properties from file system, trying classpath...", e);
            try (var is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (is != null) {
                    properties.load(is);
                } else {
                    throw new RuntimeException("Could not locate config.properties on classpath or filesystem");
                }
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load config.properties", ex);
            }
        }
    }

    private ConfigReader() {
        // Prevent instantiation
    }

    public static String get(String key) {
        // Allow command-line override via -Dkey=value
        String systemVal = System.getProperty(key);
        if (systemVal != null && !systemVal.trim().isEmpty()) {
            return systemVal.trim();
        }
        String fileVal = properties.getProperty(key);
        if (fileVal != null) {
            return fileVal.trim();
        }
        return null;
    }

    public static String get(String key, String defaultValue) {
        String val = get(key);
        return val != null ? val : defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        return val != null ? Boolean.parseBoolean(val) : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String val = get(key);
        if (val != null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }
}
