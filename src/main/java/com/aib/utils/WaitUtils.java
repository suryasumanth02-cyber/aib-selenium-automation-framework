package com.aib.utils;

import com.aib.constants.FrameworkConstants;
import com.aib.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Robust synchronization utilities replacing Thread.sleep with Explicit and Fluent waits.
 */
public final class WaitUtils {

    private WaitUtils() {
        // Prevent instantiation
    }

    public static WebElement waitForVisibility(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, FrameworkConstants.DEFAULT_TIMEOUT);
    }

    public static WebElement waitForClickability(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickability(By locator) {
        return waitForClickability(locator, FrameworkConstants.DEFAULT_TIMEOUT);
    }

    public static WebElement waitForPresence(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return waitForPresence(locator, FrameworkConstants.DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForAllVisible(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> waitForAllVisible(By locator) {
        return waitForAllVisible(locator, FrameworkConstants.DEFAULT_TIMEOUT);
    }

    public static boolean waitForTextToBePresent(By locator, String text, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForInvisibility(By locator, Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Fluent wait with custom polling interval and ignored exceptions.
     */
    public static WebElement fluentWaitForElement(By locator, Duration timeout, Duration pollingInterval) {
        FluentWait<WebDriver> wait = new FluentWait<>(DriverManager.getDriver())
                .withTimeout(timeout)
                .pollingEvery(pollingInterval)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(driver -> driver.findElement(locator));
    }
}
