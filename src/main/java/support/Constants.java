package support;

import java.time.Duration;

public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
    public static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration LONG_TIMEOUT = Duration.ofSeconds(30);

    public static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    public static final String SCREENSHOTS_DIR = "target/screenshots/";
}
