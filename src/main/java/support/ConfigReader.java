package support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {

    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(Constants.CONFIG_FILE_PATH)) {
            properties.load(fis);
            log.info("Loaded config from: {}", Constants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            try (var is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (is != null) {
                    properties.load(is);
                } else {
                    throw new RuntimeException("Could not locate config.properties");
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
        String systemVal = System.getProperty(key);
        if (systemVal != null && !systemVal.trim().isEmpty()) {
            return systemVal.trim();
        }
        String fileVal = properties.getProperty(key);
        return fileVal != null ? fileVal.trim() : null;
    }

    public static String get(String key, String defaultValue) {
        String val = get(key);
        return val != null ? val : defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        return val != null ? Boolean.parseBoolean(val) : defaultValue;
    }
}
