package com.aib.pages;

import com.aib.driver.DriverManager;
import com.aib.utils.JavaScriptUtils;
import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base Page Object containing common element interactions, CSS/RGBA inspection, and assertions.
 */
public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    @Step("Clicking on element: {locator}")
    protected void click(By locator) {
        log.info("Clicking on element: {}", locator);
        try {
            WebElement element = WaitUtils.waitForClickability(locator);
            element.click();
        } catch (Exception e) {
            log.warn("Standard click failed for {}. Attempting JavaScript click...", locator);
            WebElement element = WaitUtils.waitForPresence(locator);
            JavaScriptUtils.scrollIntoView(element);
            JavaScriptUtils.clickElement(element);
        }
    }

    @Step("Entering text '{text}' into element: {locator}")
    protected void sendKeys(By locator, String text) {
        log.info("Entering '{}' into element: {}", text, locator);
        WebElement element = WaitUtils.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Getting text from element: {locator}")
    protected String getText(By locator) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        String text = element.getText().trim();
        log.info("Element {} text: '{}'", locator, text);
        return text;
    }

    @Step("Checking if element is displayed: {locator}")
    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.waitForVisibility(locator, Duration.ofSeconds(5)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Retrieving CSS property '{propertyName}' for element: {locator}")
    public String getCssValue(By locator, String propertyName) {
        WebElement element = WaitUtils.waitForPresence(locator);
        String value = element.getCssValue(propertyName);
        log.info("Element {} CSS property '{}': '{}'", locator, propertyName, value);
        return value;
    }

    @Step("Retrieving attribute '{attributeName}' for element: {locator}")
    public String getAttribute(By locator, String attributeName) {
        WebElement element = WaitUtils.waitForPresence(locator);
        return element.getAttribute(attributeName);
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
}
