package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.DriverManager;
import support.JavaScriptUtils;
import support.WaitUtils;

import java.time.Duration;

public abstract class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    protected void click(By locator) {
        log.info("Clicking on element: {}", locator);
        try {
            WebElement element = WaitUtils.waitForClickability(locator);
            element.click();
        } catch (Exception e) {
            log.warn("Standard click failed for {}. Attempting JS click...", locator);
            WebElement element = WaitUtils.waitForPresence(locator);
            JavaScriptUtils.scrollIntoView(element);
            JavaScriptUtils.clickElement(element);
        }
    }

    protected void sendKeys(By locator, String text) {
        log.info("Entering '{}' into element: {}", text, locator);
        WebElement element = WaitUtils.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        return element.getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return WaitUtils.waitForVisibility(locator, Duration.ofSeconds(5)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCssValue(By locator, String propertyName) {
        WebElement element = WaitUtils.waitForPresence(locator);
        return element.getCssValue(propertyName);
    }

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
