package support;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Enterprise Chrome DevTools Protocol (CDP) utility for Selenium 4.
 * Uses version-resilient executeCdpCommand to bypass CDP binding incompatibilities.
 */
public final class CdpUtils {

    private static final Logger log = LoggerFactory.getLogger(CdpUtils.class);

    // Well-known Irish Geographical Coordinates
    public static final double DUBLIN_LAT = 53.349805;
    public static final double DUBLIN_LON = -6.260310;

    public static final double CORK_LAT = 51.898514;
    public static final double CORK_LON = -8.475603;

    public static final double GALWAY_LAT = 53.270668;
    public static final double GALWAY_LON = -9.056790;

    private CdpUtils() {
        // Utility class
    }

    /**
     * Emulates client geolocation coordinates via CDP and grants geolocation permission.
     *
     * @param driver    Active WebDriver instance (must be Chromium-based)
     * @param latitude  Geographical latitude
     * @param longitude Geographical longitude
     * @param accuracy  Accuracy in meters
     */
    public static void setGeolocation(WebDriver driver, double latitude, double longitude, double accuracy) {
        if (driver instanceof ChromiumDriver) {
            log.info("Emulating Geolocation via CDP: [lat={}, lon={}, accuracy={}]", latitude, longitude, accuracy);
            ChromiumDriver cDriver = (ChromiumDriver) driver;

            try {
                Map<String, Object> perm = new HashMap<>();
                perm.put("permissions", Collections.singletonList("geolocation"));
                cDriver.executeCdpCommand("Browser.grantPermissions", perm);
            } catch (Exception e) {
                log.debug("Browser.grantPermissions notice: {}", e.getMessage());
            }

            Map<String, Object> coordinates = new HashMap<>();
            coordinates.put("latitude", latitude);
            coordinates.put("longitude", longitude);
            coordinates.put("accuracy", accuracy);

            cDriver.executeCdpCommand("Emulation.setGeolocationOverride", coordinates);
        } else {
            log.warn("Geolocation emulation is only supported on Chromium-based drivers");
        }
    }

    /**
     * Clears overridden geolocation coordinates.
     */
    public static void clearGeolocation(WebDriver driver) {
        if (driver instanceof ChromiumDriver) {
            log.info("Clearing Geolocation override via CDP");
            ((ChromiumDriver) driver).executeCdpCommand("Emulation.clearGeolocationOverride", Collections.emptyMap());
        }
    }

    /**
     * Emulates network throttling conditions (e.g. Slow 3G / Fast 3G / Offline).
     *
     * @param driver             Active Chromium WebDriver
     * @param offline            Simulate offline mode if true
     * @param latencyMs          Additional network latency in milliseconds
     * @param downloadThroughput Download speed in bytes/sec (-1 for disabled)
     * @param uploadThroughput   Upload speed in bytes/sec (-1 for disabled)
     */
    public static void emulateNetworkConditions(WebDriver driver, boolean offline, int latencyMs,
                                                int downloadThroughput, int uploadThroughput) {
        if (driver instanceof ChromiumDriver) {
            log.info("Emulating Network via CDP: [offline={}, latency={}ms, download={} B/s, upload={} B/s]",
                    offline, latencyMs, downloadThroughput, uploadThroughput);

            ChromiumDriver cDriver = (ChromiumDriver) driver;
            cDriver.executeCdpCommand("Network.enable", Collections.emptyMap());

            Map<String, Object> params = new HashMap<>();
            params.put("offline", offline);
            params.put("latency", latencyMs);
            params.put("downloadThroughput", downloadThroughput);
            params.put("uploadThroughput", uploadThroughput);
            params.put("connectionType", offline ? "none" : "cellular3g");

            cDriver.executeCdpCommand("Network.emulateNetworkConditions", params);
        } else {
            log.warn("Network emulation is only supported on Chromium-based drivers");
        }
    }

    /**
     * Simulates Slow 3G network conditions (Irish rural/transit simulation).
     */
    public static void simulateSlow3G(WebDriver driver) {
        // Mobile 3G/Transit: ~200ms RTT, ~1.6 Mbps download (~200KB/s), ~200KB/s upload
        emulateNetworkConditions(driver, false, 200, 200 * 1024, 200 * 1024);
    }

    /**
     * Simulates Offline state.
     */
    public static void simulateOffline(WebDriver driver) {
        emulateNetworkConditions(driver, true, 0, 0, 0);
    }

    /**
     * Resets network conditions to unrestricted high speed.
     */
    public static void resetNetwork(WebDriver driver) {
        emulateNetworkConditions(driver, false, 0, -1, -1);
    }

    /**
     * Retrieves client JavaScript coordinates via navigator.geolocation.
     */
    public static Map<String, Object> getBrowserGeolocation(WebDriver driver) {
        String script = 
                "var callback = arguments[arguments.length - 1];" +
                "navigator.geolocation.getCurrentPosition(" +
                "  function(pos) { callback({lat: pos.coords.latitude, lon: pos.coords.longitude, accuracy: pos.coords.accuracy}); }," +
                "  function(err) { callback({error: err.message, code: err.code}); }," +
                "  {maximumAge: 0, timeout: 8000, enableHighAccuracy: true}" +
                ");";

        JavascriptExecutor js = (JavascriptExecutor) driver;
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) js.executeAsyncScript(script);
        return result;
    }

    /**
     * Extracts browser console logs and filters SEVERE errors.
     */
    public static List<LogEntry> getSevereBrowserErrors(WebDriver driver) {
        try {
            LogEntries entries = driver.manage().logs().get(LogType.BROWSER);
            return entries.getAll().stream()
                    .filter(entry -> entry.getLevel().equals(Level.SEVERE))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Could not retrieve browser logs: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
