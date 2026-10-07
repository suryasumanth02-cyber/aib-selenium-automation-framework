package support;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    public static byte[] captureScreenshotBytes() {
        if (DriverManager.getDriver() != null) {
            try {
                return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            } catch (Exception e) {
                log.warn("Failed to capture screenshot: {}", e.getMessage());
            }
        }
        return new byte[0];
    }

    public static String saveScreenshotToFile(String testName, String status) {
        if (DriverManager.getDriver() == null) {
            return null;
        }

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String subDir = status.equalsIgnoreCase("PASSED") ? "passed/" : "failed/";
            String fileName = testName + "_" + timestamp + ".png";
            String fullPath = Constants.SCREENSHOTS_DIR + subDir + fileName;

            File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            File destFile = new File(fullPath);

            FileUtils.copyFile(srcFile, destFile);
            return fullPath;
        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage());
            return null;
        }
    }
}
