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
 * Screenshot utility for capturing failure evidence and attaching to Allure reports.
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    /**
     * Captures screenshot as byte array and attaches directly to Allure report.
     */
    @Attachment(value = "Failure Screenshot: {testName}", type = "image/png")
    public static byte[] captureScreenshotForReport(String testName) {
        if (DriverManager.getDriver() != null) {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Saves screenshot to target directory.
     */
    public static String saveScreenshotToFile(String testName) {
        if (DriverManager.getDriver() == null) {
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = FrameworkConstants.SCREENSHOTS_DIR + fileName;

        File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
        File destFile = new File(filePath);

        try {
            FileUtils.copyFile(srcFile, destFile);
            log.info("Saved failure screenshot to: {}", destFile.getAbsolutePath());
            return filePath;
        } catch (IOException e) {
            log.error("Failed to save screenshot file", e);
            return null;
        }
    }
}
