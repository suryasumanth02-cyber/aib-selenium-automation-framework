package com.aib.driver;

import com.aib.config.ConfigReader;
import com.aib.enums.BrowserType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Factory class responsible for initializing browser instances based on configuration.
 */
public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final String DEFAULT_USER_AGENT = 
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";

    private DriverFactory() {
        // Prevent instantiation
    }

    public static WebDriver createDriver(String browserName, boolean isHeadless) {
        BrowserType browserType;
        try {
            browserType = BrowserType.valueOf(browserName.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid browser '{}' specified. Defaulting to CHROME.", browserName);
            browserType = BrowserType.CHROME;
        }

        log.info("Initializing {} browser (Headless: {})", browserType, isHeadless);

        switch (browserType) {
            case FIREFOX:
                return createFirefoxDriver(isHeadless);
            case EDGE:
                return createEdgeDriver(isHeadless);
            case CHROME:
            default:
                return createChromeDriver(isHeadless);
        }
    }

    private static WebDriver createChromeDriver(boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();

        if (isHeadless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--user-agent=" + DEFAULT_USER_AGENT);

        // Anti-bot detection stealth options
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean isHeadless) {
        FirefoxOptions options = new FirefoxOptions();
        if (isHeadless) {
            options.addArguments("-headless");
        }
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        options.addPreference("general.useragent.override", DEFAULT_USER_AGENT);
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean isHeadless) {
        EdgeOptions options = new EdgeOptions();
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=" + DEFAULT_USER_AGENT);
        return new EdgeDriver(options);
    }
}
