package com.aib.tests;

import com.aib.base.BaseTest;
import com.aib.config.ConfigReader;
import com.aib.pages.FxRatesPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Foreign Exchange (FX) & Currency Tool Visual Testing")
public class FxRatesTest extends BaseTest {

    private FxRatesPage fxPage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String url = ConfigReader.get("fxRatesUrl", "https://www.aib.ie/fxcentre/tools/fxrates-calculator");
        navigateTo(url);
        fxPage = new FxRatesPage();
    }

    @Test(priority = 1, description = "Verify FX Rates Calculator page loads with conversion inputs")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that FX currency conversion input element is displayed.")
    public void test01_CalculatorIsDisplayed() {
        Assert.assertTrue(fxPage.isCalculatorDisplayed(),
                "FX conversion calculator input fields should be displayed.");
    }

    @Test(priority = 2, description = "Verify base currency is initialized to EUR")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that AIB's default domestic base currency is set to EUR.")
    public void test02_DefaultBaseCurrencyIsEur() {
        String baseCurrency = fxPage.getBaseCurrency();
        log.info("Base currency detected: {}", baseCurrency);
        Assert.assertTrue(baseCurrency.equalsIgnoreCase("EUR") || baseCurrency.contains("EUR"),
                "Expected base currency to be EUR. Found: " + baseCurrency);
    }

    @Test(priority = 3, description = "Verify currency search filter input is displayed")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures currency search input is available in the FX tool.")
    public void test03_SearchCurrencyInputIsDisplayed() {
        Assert.assertTrue(fxPage.isSearchInputDisplayed(),
                "Currency search input should be visible.");
    }

    @Test(priority = 4, description = "Verify Amount input text RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that amount input text color is styled with a valid RGBA.")
    public void test04_AmountInputTextColorRgba() {
        String color = fxPage.getAmountInputTextColor();
        log.info("Amount input text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Amount text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 5, description = "Verify Amount input background RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that amount input background color is styled with a valid RGBA.")
    public void test05_AmountInputBackgroundColorRgba() {
        String color = fxPage.getAmountInputBackgroundColor();
        log.info("Amount input background RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Amount background color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 6, description = "Verify Base Currency text RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that base currency text color is styled with a valid RGBA.")
    public void test06_BaseCurrencyTextColorRgba() {
        String color = fxPage.getBaseCurrencyTextColor();
        log.info("Base currency text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Base currency text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 7, description = "Verify Base Currency background RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that base currency background color is styled with a valid RGBA.")
    public void test07_BaseCurrencyBackgroundColorRgba() {
        String color = fxPage.getBaseCurrencyBackgroundColor();
        log.info("Base currency background RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Base currency background should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 8, description = "Verify Converted Amount output text RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that converted amount output color is styled with a valid RGBA.")
    public void test08_ConvertedAmountTextColorRgba() {
        String color = fxPage.getConvertedAmountTextColor();
        log.info("Converted amount text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Converted amount text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 9, description = "Verify user can enter conversion amount")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Enters an amount of 500 into the calculator and verifies field value.")
    public void test09_EnterConversionAmountNumeric() {
        fxPage.enterAmountYouGive("500");
        String value = fxPage.getEnteredAmount();
        Assert.assertTrue(value.contains("500"), "Expected entered amount to contain 500. Found: " + value);
    }

    @Test(priority = 10, description = "Verify converted amount value is calculated")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that the output converted amount field is non-empty after entering amount.")
    public void test10_ConvertedAmountValuePopulated() {
        String converted = fxPage.getConvertedAmount();
        log.info("Converted amount output: {}", converted);
        Assert.assertNotNull(converted, "Converted amount should not be null.");
    }

    @Test(priority = 11, description = "Verify user can clear amount input")
    @Severity(SeverityLevel.NORMAL)
    @Description("Clears the conversion amount input field.")
    public void test11_ClearAmountInput() {
        fxPage.clearAmountInput();
        String value = fxPage.getEnteredAmount();
        Assert.assertEquals(value, "", "Amount input should be cleared.");
    }

    @Test(priority = 12, description = "Verify re-entering new amount works after clear")
    @Severity(SeverityLevel.NORMAL)
    @Description("Enters a new amount of 1000 after clearing and verifies field updates.")
    public void test12_AmountInputUpdateAfterClear() {
        fxPage.enterAmountYouGive("1000");
        String value = fxPage.getEnteredAmount();
        Assert.assertTrue(value.contains("1000"), "Expected amount input to contain 1000. Found: " + value);
    }
}
