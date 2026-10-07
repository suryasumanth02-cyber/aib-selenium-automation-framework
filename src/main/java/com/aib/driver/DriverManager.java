package com.aib.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-safe WebDriver storage using ThreadLocal.
 * Crucial for executing tests in parallel across multiple threads without collision.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    /**
     * Gets the current thread's WebDriver instance.
     */
    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Sets the WebDriver instance for the current thread.
     */
    public static void setDriver(WebDriver driver) {
        DRIVER_THREAD_LOCAL.set(driver);
    }

    /**
     * Removes the WebDriver reference from ThreadLocal to prevent memory leaks.
     */
    public static void unload() {
        DRIVER_THREAD_LOCAL.remove();
    }
}
