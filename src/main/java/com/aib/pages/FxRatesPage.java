package com.aib.pages;

import com.aib.utils.JavaScriptUtils;
import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Page Object for AIB FX Rates & Currency Calculator (https://www.aib.ie/fxcentre/tools/fxrates-calculator).
 */
public class FxRatesPage extends BasePage {

    // Locators
    private final By baseCurrencyInput = By.id("base-currency");
    private final By amountYouGiveInput = By.id("amount-you-give");
    private final By amountGiveYouInput = By.id("amount-give-you");
    private final By searchInput = By.id("search-input");
    private final By calculatorTitle = By.xpath("//h1 | //h2[contains(text(), 'FX')]");

    @Step("Verifying that the FX Calculator page is displayed")
    public boolean isCalculatorDisplayed() {
        return isDisplayed(amountYouGiveInput);
    }

    @Step("Entering conversion amount: {amount}")
    public FxRatesPage enterAmountYouGive(String amount) {
        log.info("Entering amount to convert: {}", amount);
        WebElement input = WaitUtils.waitForVisibility(amountYouGiveInput);
        input.clear();
        input.sendKeys(amount);
        return this;
    }

    @Step("Retrieving converted amount from output field")
    public String getConvertedAmount() {
        WebElement input = WaitUtils.waitForPresence(amountGiveYouInput);
        String value = input.getAttribute("value");
        log.info("Converted amount returned: '{}'", value);
        return value;
    }

    @Step("Retrieving base currency value")
    public String getBaseCurrency() {
        WebElement input = WaitUtils.waitForPresence(baseCurrencyInput);
        return input.getAttribute("value");
    }
}
