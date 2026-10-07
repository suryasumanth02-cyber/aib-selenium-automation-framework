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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Branch Locator & RGBA Visual Testing")
public class BranchLocatorTest extends BaseTest {

    private BranchLocatorPage branchPage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String url = ConfigReader.get("branchLocatorUrl", "https://branches.aib.ie/search");
        navigateTo(url);
        branchPage = new BranchLocatorPage();
    }

    @Test(priority = 1, description = "Verify Branch Locator search input is available")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that search input element is rendered.")
    public void test01_SearchInputIsDisplayed() {
        Assert.assertTrue(branchPage.isSearchInputDisplayed(),
                "Branch locator search input should be visible.");
    }

    @Test(priority = 2, description = "Verify Search input placeholder contains text")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures placeholder attribute contains helper text.")
    public void test02_SearchInputPlaceholderText() {
        String placeholder = branchPage.getSearchInputPlaceholder();
        Assert.assertNotNull(placeholder, "Placeholder should not be null.");
        Assert.assertTrue(placeholder.length() > 0, "Placeholder should not be empty.");
    }

    @Test(priority = 3, description = "Verify Search input background RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that search input background CSS color is a valid RGBA.")
    public void test03_SearchInputBackgroundColorRgba() {
        String bgColor = branchPage.getSearchInputBackgroundColor();
        log.info("Search input background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Search input background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 4, description = "Verify Search input text RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that search input font color is a valid RGBA.")
    public void test04_SearchInputTextColorRgba() {
        String textColor = branchPage.getSearchInputTextColor();
        log.info("Search input text RGBA: {}", textColor);
        Assert.assertTrue(textColor.startsWith("rgb") || textColor.startsWith("rgba"),
                "Search input text color should be in rgb/rgba format. Found: " + textColor);
    }

    @Test(priority = 5, description = "Verify Search button background RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that search button background is formatted as valid RGBA.")
    public void test05_SearchButtonBackgroundColorRgba() {
        String bgColor = branchPage.getSearchButtonBackgroundColor();
        log.info("Search button background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Search button background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 6, description = "Verify Search button text/icon RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that search button foreground color is valid RGBA.")
    public void test06_SearchButtonTextColorRgba() {
        String textColor = branchPage.getSearchButtonTextColor();
        log.info("Search button text RGBA: {}", textColor);
        Assert.assertTrue(textColor.startsWith("rgb") || textColor.startsWith("rgba"),
                "Search button text color should be in rgb/rgba format. Found: " + textColor);
    }

    @DataProvider(name = "irishLocations")
    public Object[][] getIrishLocations() {
        return new Object[][]{
                {"Dublin"},
                {"Cork"},
                {"Galway"},
                {"Limerick"},
                {"Waterford"},
                {"Kilkenny"},
                {"Dundalk"},
                {"Sligo"}
        };
    }

    @Test(priority = 7, dataProvider = "irishLocations", description = "Verify branch search returns locations across Ireland")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Searches for various Irish towns/cities and confirms that branch location cards are returned.")
    public void test07_SearchBranchesAcrossIreland(String location) {
        branchPage.searchLocation(location);
        int count = branchPage.getResultsCount();
        log.info("Location '{}' returned {} results", location, count);
        Assert.assertTrue(count > 0, "Expected at least 1 branch for: " + location);
    }
}
