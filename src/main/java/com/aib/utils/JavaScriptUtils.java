package com.aib.utils;

import com.aib.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * JavaScriptExecutor helper utilities for handling stubborn elements, scrolling, and page state.
 */
public final class JavaScriptUtils {

    private JavaScriptUtils() {
        // Prevent instantiation
    }

    private static JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) DriverManager.getDriver();
    }

    public static void scrollIntoView(WebElement element) {
        getJsExecutor().executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public static void clickElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].click();", element);
    }

    public static void setInputValue(WebElement element, String value) {
        getJsExecutor().executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", element, value);
    }

    public static void highlightElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].style.border='3px solid red';", element);
    }

    public static boolean waitForPageLoad(Duration timeout) {
        return new WebDriverWait(DriverManager.getDriver(), timeout).until(driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public static String getDocumentTitle() {
        return (String) getJsExecutor().executeScript("return document.title;");
    }
}
