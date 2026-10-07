package com.aib.constants;

import java.time.Duration;

/**
 * Global framework constants.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        // Prevent instantiation
    }

    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
    public static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);
    public static final Duration LONG_TIMEOUT = Duration.ofSeconds(30);
    public static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

    public static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    public static final String SCREENSHOTS_DIR = "target/screenshots/";
    public static final String ALLURE_RESULTS_DIR = "target/allure-results/";
}
