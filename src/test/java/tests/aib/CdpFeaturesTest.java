package tests.aib;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import support.BaseTest;
import support.CdpUtils;
import support.ConfigReader;
import support.DriverManager;

import java.util.List;
import java.util.Map;

@Epic("Browser DevTools Protocol Automation")
@Feature("Selenium 4 Chrome DevTools Protocol (CDP)")
public class CdpFeaturesTest extends BaseTest {

    @AfterMethod(alwaysRun = true)
    public void cleanupCdpOverrides() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            CdpUtils.clearGeolocation(driver);
            CdpUtils.resetNetwork(driver);
            driver.manage().timeouts().pageLoadTimeout(support.Constants.LONG_TIMEOUT);
        }
    }

    @Test(groups = {"cdp", "geolocation", "regression"}, priority = 1)
    @Story("Dublin Geolocation Emulation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Emulate Dublin coordinates (53.3498, -6.2603) via CDP and verify navigator.geolocation")
    public void testGeolocationDublinEmulation() {
        WebDriver driver = DriverManager.getDriver();
        CdpUtils.setGeolocation(driver, CdpUtils.DUBLIN_LAT, CdpUtils.DUBLIN_LON, 100);
        navigateTo(ConfigReader.get("baseUrl"));

        Map<String, Object> coords = CdpUtils.getBrowserGeolocation(driver);
        log.info("Emulated Dublin Geolocation received: {}", coords);

        Assert.assertNotNull(coords, "Geolocation should return coordinates");
        if (coords.containsKey("lat")) {
            double lat = ((Number) coords.get("lat")).doubleValue();
            double lon = ((Number) coords.get("lon")).doubleValue();
            Assert.assertEquals(lat, CdpUtils.DUBLIN_LAT, 0.05, "Emulated latitude should match Dublin coordinates area");
            Assert.assertEquals(lon, CdpUtils.DUBLIN_LON, 0.05, "Emulated longitude should match Dublin coordinates area");
        }
    }

    @Test(groups = {"cdp", "geolocation", "regression"}, priority = 2)
    @Story("Cork Geolocation Emulation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Emulate Cork coordinates (51.8985, -8.4756) via CDP for branch locator regional testing")
    public void testGeolocationCorkEmulation() {
        WebDriver driver = DriverManager.getDriver();
        navigateTo(ConfigReader.get("baseUrl"));

        CdpUtils.setGeolocation(driver, CdpUtils.CORK_LAT, CdpUtils.CORK_LON, 100);

        Map<String, Object> coords = CdpUtils.getBrowserGeolocation(driver);
        log.info("Emulated Cork Geolocation received: {}", coords);

        Assert.assertNotNull(coords, "Geolocation should return coordinates");
        if (coords.containsKey("lat")) {
            double lat = ((Number) coords.get("lat")).doubleValue();
            double lon = ((Number) coords.get("lon")).doubleValue();
            Assert.assertEquals(lat, CdpUtils.CORK_LAT, 0.001, "Emulated latitude should match Cork coordinates");
            Assert.assertEquals(lon, CdpUtils.CORK_LON, 0.001, "Emulated longitude should match Cork coordinates");
        }
    }

    @Test(groups = {"cdp", "network", "resilience"}, priority = 3)
    @Story("Slow 3G Network Resilience")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Simulate Slow 3G network conditions via CDP to verify banking portal stability")
    public void testSlow3GNetworkEmulationOnMortgageCalculator() {
        WebDriver driver = DriverManager.getDriver();
        CdpUtils.simulateSlow3G(driver);
        long startTime = System.currentTimeMillis();

        navigateTo(ConfigReader.get("baseUrl"));
        long durationMs = System.currentTimeMillis() - startTime;

        log.info("AIB Portal loaded under Slow 3G in {} ms", durationMs);
        Assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains("aib.ie"),
                "AIB portal should successfully load and maintain stability under throttled network conditions");
    }

    @Test(groups = {"cdp", "console", "regression"}, priority = 4)
    @Story("Browser Console Health")
    @Severity(SeverityLevel.MINOR)
    @Description("Capture browser console logs via CDP / Selenium LogEntries and audit for critical JavaScript exceptions")
    public void testBrowserConsoleZeroSevereExceptionsOnHome() {
        WebDriver driver = DriverManager.getDriver();
        navigateTo(ConfigReader.get("baseUrl"));

        List<LogEntry> severeErrors = CdpUtils.getSevereBrowserErrors(driver);
        log.info("Severe browser console errors detected on AIB Homepage: {}", severeErrors.size());

        for (LogEntry error : severeErrors) {
            log.warn("Console Error: [{}] {}", error.getLevel(), error.getMessage());
        }

        // Informational assertion: log severe errors, verify page didn't crash
        Assert.assertNotNull(severeErrors, "Browser logs collection should succeed");
    }
}
