package com.aib.pages;

import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object for AIB FX Rates & Currency Calculator with RGBA and conversion validation.
 */
public class FxRatesPage extends BasePage {

    // Locators
    private final By baseCurrencyInput = By.id("base-currency");
    private final By amountYouGiveInput = By.id("amount-you-give");
    private final By amountGiveYouInput = By.id("amount-give-you");
    private final By searchInput = By.id("search-input");
    private final By fxCalculatorContainer = By.xpath("//div[contains(@class, 'fx-calculator') or contains(@class, 'calculator')]");

    @Step("Verifying that the FX Calculator page is displayed")
    public boolean isCalculatorDisplayed() {
        return isDisplayed(amountYouGiveInput);
    }

    @Step("Verifying that FX currency search input is displayed")
    public boolean isSearchInputDisplayed() {
        return isDisplayed(searchInput);
    }

    @Step("Entering conversion amount: {amount}")
    public FxRatesPage enterAmountYouGive(String amount) {
        log.info("Entering amount to convert: {}", amount);
        WebElement input = WaitUtils.waitForVisibility(amountYouGiveInput);
        input.clear();
        input.sendKeys(amount);
        return this;
    }

    @Step("Clearing conversion amount input")
    public FxRatesPage clearAmountInput() {
        WebElement input = WaitUtils.waitForVisibility(amountYouGiveInput);
        input.clear();
        return this;
    }

    @Step("Retrieving amount entered in the input field")
    public String getEnteredAmount() {
        WebElement input = WaitUtils.waitForPresence(amountYouGiveInput);
        return input.getAttribute("value");
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

    @Step("Retrieving Amount input text color RGBA")
    public String getAmountInputTextColor() {
        return getCssValue(amountYouGiveInput, "color");
    }

    @Step("Retrieving Amount input background color RGBA")
    public String getAmountInputBackgroundColor() {
        return getCssValue(amountYouGiveInput, "background-color");
    }

    @Step("Retrieving Base Currency text color RGBA")
    public String getBaseCurrencyTextColor() {
        return getCssValue(baseCurrencyInput, "color");
    }

    @Step("Retrieving Base Currency background color RGBA")
    public String getBaseCurrencyBackgroundColor() {
        return getCssValue(baseCurrencyInput, "background-color");
    }

    @Step("Retrieving Converted Amount output text color RGBA")
    public String getConvertedAmountTextColor() {
        return getCssValue(amountGiveYouInput, "color");
    }
}
