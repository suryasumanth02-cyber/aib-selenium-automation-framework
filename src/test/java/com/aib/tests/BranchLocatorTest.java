package com.aib.tests;

import com.aib.base.BaseTest;
import com.aib.config.ConfigReader;
import com.aib.pages.BranchLocatorPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Branch & ATM Finder")
public class BranchLocatorTest extends BaseTest {

    @Test(priority = 1, description = "Verify Branch Locator search input is available")
    @Severity(SeverityLevel.NORMAL)
    @Description("Navigates to AIB Branch Locator and validates that the search input is loaded and ready.")
    public void testBranchLocatorSearchInputLoaded() {
        String url = ConfigReader.get("branchLocatorUrl", "https://branches.aib.ie/search");
        navigateTo(url);

        BranchLocatorPage branchLocatorPage = new BranchLocatorPage();
        Assert.assertTrue(branchLocatorPage.isSearchInputDisplayed(),
                "Branch locator search input should be visible.");
    }

    @DataProvider(name = "locationsData")
    public Object[][] getLocationsData() {
        return new Object[][]{
                {"Dublin"},
                {"Cork"}
        };
    }

    @Test(priority = 2, dataProvider = "locationsData", description = "Verify branch search returns locations for Irish regions")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Performs search for Irish cities/counties and verifies that matching branch cards are rendered.")
    public void testBranchSearch(String location) {
        String url = ConfigReader.get("branchLocatorUrl", "https://branches.aib.ie/search");
        navigateTo(url);

        BranchLocatorPage branchLocatorPage = new BranchLocatorPage();
        branchLocatorPage.searchLocation(location);

        int count = branchLocatorPage.getResultsCount();
        log.info("Found {} branch results for location: '{}'", count, location);
        Assert.assertTrue(count > 0, "Expected at least 1 branch to be returned for: " + location);
    }
}
