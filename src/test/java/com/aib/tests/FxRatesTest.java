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
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Foreign Exchange (FX) & Currency Calculator")
public class FxRatesTest extends BaseTest {

    @Test(priority = 1, description = "Verify FX Rates Calculator page loads with conversion inputs")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that the public FX currency conversion tool loads with currency inputs.")
    public void testFxRatesCalculatorLoads() {
        String url = ConfigReader.get("fxRatesUrl", "https://www.aib.ie/fxcentre/tools/fxrates-calculator");
        navigateTo(url);

        FxRatesPage fxPage = new FxRatesPage();
        Assert.assertTrue(fxPage.isCalculatorDisplayed(),
                "FX conversion calculator input fields should be displayed.");
    }

    @Test(priority = 2, description = "Verify base currency is initialized to EUR")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that AIB's default domestic base currency is set to EUR.")
    public void testDefaultBaseCurrencyIsEur() {
        String url = ConfigReader.get("fxRatesUrl", "https://www.aib.ie/fxcentre/tools/fxrates-calculator");
        navigateTo(url);

        FxRatesPage fxPage = new FxRatesPage();
        String baseCurrency = fxPage.getBaseCurrency();
        log.info("Base currency detected: {}", baseCurrency);
        Assert.assertTrue(baseCurrency.equalsIgnoreCase("EUR") || baseCurrency.contains("EUR"),
                "Expected base currency to be EUR. Found: " + baseCurrency);
    }
}
