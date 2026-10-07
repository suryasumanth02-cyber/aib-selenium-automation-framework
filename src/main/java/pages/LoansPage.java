package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import support.WaitUtils;

public class LoansPage extends BasePage {

    private final By baseCurrencyInput = By.id("base-currency");
    private final By amountYouGiveInput = By.id("amount-you-give");
    private final By amountGiveYouInput = By.id("amount-give-you");
    private final By searchInput = By.id("search-input");

    public boolean isCalculatorDisplayed() {
        return isDisplayed(amountYouGiveInput);
    }

    public boolean isSearchInputDisplayed() {
        return isDisplayed(searchInput);
    }

    public LoansPage enterAmount(String amount) {
        WebElement input = WaitUtils.waitForVisibility(amountYouGiveInput);
        input.clear();
        input.sendKeys(amount);
        return this;
    }

    public LoansPage clearAmount() {
        WebElement input = WaitUtils.waitForVisibility(amountYouGiveInput);
        input.clear();
        return this;
    }

    public String getEnteredAmount() {
        WebElement input = WaitUtils.waitForPresence(amountYouGiveInput);
        return input.getAttribute("value");
    }

    public String getConvertedAmount() {
        WebElement input = WaitUtils.waitForPresence(amountGiveYouInput);
        return input.getAttribute("value");
    }

    public String getBaseCurrency() {
        WebElement input = WaitUtils.waitForPresence(baseCurrencyInput);
        return input.getAttribute("value");
    }

    public String getAmountTextColor() {
        return getCssValue(amountYouGiveInput, "color");
    }

    public String getAmountBackgroundColor() {
        return getCssValue(amountYouGiveInput, "background-color");
    }

    public String getBaseCurrencyTextColor() {
        return getCssValue(baseCurrencyInput, "color");
    }

    public String getBaseCurrencyBackgroundColor() {
        return getCssValue(baseCurrencyInput, "background-color");
    }

    public String getConvertedAmountTextColor() {
        return getCssValue(amountGiveYouInput, "color");
    }
}
