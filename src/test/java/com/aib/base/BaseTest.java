package com.aib.base;

import com.aib.config.ConfigReader;
import com.aib.constants.FrameworkConstants;
import com.aib.driver.DriverFactory;
import com.aib.driver.DriverManager;
import com.aib.listeners.TestListener;
import com.aib.pages.CookieBannerComponent;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void setUp(@Optional String browserParam, @Optional String headlessParam) {
        String browser = (browserParam != null && !browserParam.isEmpty())
                ? browserParam
                : ConfigReader.get("browser", "chrome");

        boolean headless = (headlessParam != null && !headlessParam.isEmpty())
                ? Boolean.parseBoolean(headlessParam)
                : ConfigReader.getBoolean("headless", true);

        log.info("Setting up WebDriver for thread ID: {} [Browser: {}, Headless: {}]",
                Thread.currentThread().getId(), browser, headless);

        WebDriver driver = DriverFactory.createDriver(browser, headless);
        DriverManager.setDriver(driver);

        driver.manage().timeouts().implicitlyWait(FrameworkConstants.SHORT_TIMEOUT);
        driver.manage().timeouts().pageLoadTimeout(FrameworkConstants.LONG_TIMEOUT);

        if (!headless) {
            driver.manage().window().maximize();
        }
    }

    /**
     * Helper to navigate to a URL and handle GDPR cookie banner automatically.
     */
    protected void navigateTo(String url) {
        log.info("Navigating to URL: {}", url);
        DriverManager.getDriver().get(url);
        new CookieBannerComponent().acceptCookiesIfPresent();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            log.info("Tearing down WebDriver for thread ID: {}", Thread.currentThread().getId());
            try {
                driver.quit();
            } catch (Exception e) {
                log.warn("Error while closing driver: {}", e.getMessage());
            } finally {
                DriverManager.unload();
            }
        }
    }
}
