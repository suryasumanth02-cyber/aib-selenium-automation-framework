package com.aib.utils;

import com.aib.constants.FrameworkConstants;
import com.aib.driver.DriverManager;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot utility capturing screenshots for both Passed and Failed tests.
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    /**
     * Captures screenshot as byte array and attaches directly to Allure report.
     */
    @Attachment(value = "{status} Screenshot: {testName}", type = "image/png")
    public static byte[] captureScreenshotForReport(String testName, String status) {
        if (DriverManager.getDriver() != null) {
            try {
                return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            } catch (Exception e) {
                log.warn("Failed to capture screenshot bytes: {}", e.getMessage());
            }
        }
        return new byte[0];
    }

    /**
     * Saves screenshot to target directory categorized by status (passed / failed).
     */
    public static String saveScreenshotToFile(String testName, String status) {
        if (DriverManager.getDriver() == null) {
            return null;
        }

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String subDir = status.equalsIgnoreCase("PASSED") ? "passed/" : "failed/";
            String fileName = testName + "_" + timestamp + ".png";
            String fullPath = FrameworkConstants.SCREENSHOTS_DIR + subDir + fileName;

            File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            File destFile = new File(fullPath);

            FileUtils.copyFile(srcFile, destFile);
            log.info("Saved {} screenshot to: {}", status, destFile.getAbsolutePath());
            return fullPath;
        } catch (Exception e) {
            log.error("Failed to save screenshot file: {}", e.getMessage());
            return null;
        }
    }
}
