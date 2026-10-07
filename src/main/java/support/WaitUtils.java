package support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class WaitUtils {

    private WaitUtils() {
        // Prevent instantiation
    }

    public static WebElement waitForVisibility(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, Constants.DEFAULT_TIMEOUT);
    }

    public static WebElement waitForClickability(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickability(By locator) {
        return waitForClickability(locator, Constants.DEFAULT_TIMEOUT);
    }

    public static WebElement waitForPresence(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return waitForPresence(locator, Constants.DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForAllVisible(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForInvisibility(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
