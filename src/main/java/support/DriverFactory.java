package support;

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

public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final String DEFAULT_USER_AGENT = 
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";

    private DriverFactory() {
        // Prevent instantiation
    }

    public static WebDriver createDriver(String browserName, boolean isHeadless) {
        String browser = (browserName == null) ? "CHROME" : browserName.trim().toUpperCase();
        log.info("Initializing {} browser (Headless: {})", browser, isHeadless);

        switch (browser) {
            case "FIREFOX":
                FirefoxOptions fOptions = new FirefoxOptions();
                if (isHeadless) fOptions.addArguments("-headless");
                fOptions.addArguments("--width=1920", "--height=1080");
                fOptions.addPreference("general.useragent.override", DEFAULT_USER_AGENT);
                return new FirefoxDriver(fOptions);

            case "EDGE":
                EdgeOptions eOptions = new EdgeOptions();
                if (isHeadless) eOptions.addArguments("--headless=new");
                eOptions.addArguments("--window-size=1920,1080", "--start-maximized", "--disable-blink-features=AutomationControlled");
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
                return new ChromeDriver(cOptions);
        }
    }
}
