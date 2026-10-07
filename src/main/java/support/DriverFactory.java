package support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URL;
import java.util.Collections;

public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final String DEFAULT_USER_AGENT = 
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";

    private DriverFactory() {
        // Prevent instantiation
    }

    public static WebDriver createDriver(String browserName, boolean isHeadless) {
        String browser = (browserName == null) ? "CHROME" : browserName.trim().toUpperCase();
        boolean isRemote = Boolean.parseBoolean(System.getProperty("remote", ConfigReader.get("remote", "false")));
        String gridUrl = System.getProperty("grid.url", ConfigReader.get("gridUrl", "http://localhost:4444/wd/hub"));

        log.info("Initializing {} browser (Headless: {}, Remote Grid: {})", browser, isHeadless, isRemote);

        switch (browser) {
            case "FIREFOX":
                FirefoxOptions fOptions = new FirefoxOptions();
                if (isHeadless) fOptions.addArguments("-headless");
                fOptions.addArguments("--width=1920", "--height=1080");
                fOptions.addPreference("general.useragent.override", DEFAULT_USER_AGENT);
                if (isRemote) {
                    return createRemoteDriver(gridUrl, fOptions);
                }
                return new FirefoxDriver(fOptions);

            case "EDGE":
                EdgeOptions eOptions = new EdgeOptions();
                if (isHeadless) eOptions.addArguments("--headless=new");
                eOptions.addArguments("--window-size=1920,1080", "--start-maximized", "--disable-blink-features=AutomationControlled");
                if (isRemote) {
                    return createRemoteDriver(gridUrl, eOptions);
                }
                return new EdgeDriver(eOptions);

            case "CHROME":
            default:
                ChromeOptions cOptions = new ChromeOptions();
                if (isHeadless) cOptions.addArguments("--headless=new");
                cOptions.addArguments("--window-size=1920,1080", "--start-maximized", "--disable-gpu",
                        "--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*",
                        "--user-agent=" + DEFAULT_USER_AGENT, "--disable-blink-features=AutomationControlled");
                cOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                cOptions.setExperimentalOption("useAutomationExtension", false);
                if (isRemote) {
                    return createRemoteDriver(gridUrl, cOptions);
                }
                return new ChromeDriver(cOptions);
        }
    }

    private static WebDriver createRemoteDriver(String gridUrl, org.openqa.selenium.Capabilities capabilities) {
        log.info("Connecting to Selenium Grid Hub at: {}", gridUrl);
        try {
            URL hubUrl = URI.create(gridUrl).toURL();
            return new RemoteWebDriver(hubUrl, capabilities);
        } catch (Exception e) {
            log.error("Failed to create RemoteWebDriver session on grid {}: {}", gridUrl, e.getMessage());
            throw new RuntimeException("Selenium Grid connection error: " + e.getMessage(), e);
        }
    }
}
