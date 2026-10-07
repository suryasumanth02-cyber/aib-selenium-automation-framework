package tests.aib;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoansPage;
import support.BaseTest;
import support.ConfigReader;

public class LoansTest extends BaseTest {

    private LoansPage loansPage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String url = ConfigReader.get("fxRatesUrl", "https://www.aib.ie/fxcentre/tools/fxrates-calculator");
        navigateTo(url);
        loansPage = new LoansPage();
    }

    @Test(priority = 1, groups = {"loans", "ui"}, description = "Verify Loans / FX Rates Calculator page loads")
    public void test01_CalculatorIsDisplayed() {
        Assert.assertTrue(loansPage.isCalculatorDisplayed(),
                "FX conversion calculator input fields should be displayed.");
    }

    @Test(priority = 2, groups = {"loans", "functional"}, description = "Verify base currency is initialized to EUR")
    public void test02_DefaultBaseCurrencyIsEur() {
        String baseCurrency = loansPage.getBaseCurrency();
        log.info("Base currency detected: {}", baseCurrency);
        Assert.assertTrue(baseCurrency.equalsIgnoreCase("EUR") || baseCurrency.contains("EUR"),
                "Expected base currency to be EUR. Found: " + baseCurrency);
    }

    @Test(priority = 3, groups = {"loans", "ui"}, description = "Verify currency search filter input is displayed")
    public void test03_SearchCurrencyInputIsDisplayed() {
        Assert.assertTrue(loansPage.isSearchInputDisplayed(),
                "Currency search input should be visible.");
    }

    @Test(priority = 4, groups = {"loans", "rgba"}, description = "Verify Amount input text RGBA color")
    public void test04_AmountInputTextColorRgba() {
        String color = loansPage.getAmountTextColor();
        log.info("Amount input text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Amount text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 5, groups = {"loans", "rgba"}, description = "Verify Amount input background RGBA color")
    public void test05_AmountInputBackgroundColorRgba() {
        String color = loansPage.getAmountBackgroundColor();
        log.info("Amount input background RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Amount background color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 6, groups = {"loans", "rgba"}, description = "Verify Base Currency text RGBA color")
    public void test06_BaseCurrencyTextColorRgba() {
        String color = loansPage.getBaseCurrencyTextColor();
        log.info("Base currency text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Base currency text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 7, groups = {"loans", "rgba"}, description = "Verify Base Currency background RGBA color")
    public void test07_BaseCurrencyBackgroundColorRgba() {
        String color = loansPage.getBaseCurrencyBackgroundColor();
        log.info("Base currency background RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Base currency background should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 8, groups = {"loans", "rgba"}, description = "Verify Converted Amount output text RGBA color")
    public void test08_ConvertedAmountTextColorRgba() {
        String color = loansPage.getConvertedAmountTextColor();
        log.info("Converted amount text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Converted amount text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 9, groups = {"loans", "functional"}, description = "Verify user can enter conversion amount")
    public void test09_EnterConversionAmountNumeric() {
        loansPage.enterAmount("500");
        String value = loansPage.getEnteredAmount();
        Assert.assertTrue(value.contains("500"), "Expected entered amount to contain 500. Found: " + value);
    }

    @Test(priority = 10, groups = {"loans", "functional"}, description = "Verify converted amount value is calculated")
    public void test10_ConvertedAmountValuePopulated() {
        String converted = loansPage.getConvertedAmount();
        log.info("Converted amount output: {}", converted);
        Assert.assertNotNull(converted, "Converted amount should not be null.");
    }

    @Test(priority = 11, groups = {"loans", "functional"}, description = "Verify user can clear amount input")
    public void test11_ClearAmountInput() {
        loansPage.clearAmount();
        String value = loansPage.getEnteredAmount();
        Assert.assertEquals(value, "", "Amount input should be cleared.");
    }

    @Test(priority = 12, groups = {"loans", "functional"}, description = "Verify re-entering new amount works after clear")
    public void test12_AmountInputUpdateAfterClear() {
        loansPage.enterAmount("1000");
        String value = loansPage.getEnteredAmount();
        Assert.assertTrue(value.contains("1000"), "Expected amount input to contain 1000. Found: " + value);
    }
}
