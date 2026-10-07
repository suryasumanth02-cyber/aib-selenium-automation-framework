package tests.aib;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SavingsPage;
import support.BaseTest;
import support.ConfigReader;

public class SavingsTest extends BaseTest {

    private SavingsPage savingsPage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String url = ConfigReader.get("branchLocatorUrl", "https://branches.aib.ie/search");
        navigateTo(url);
        savingsPage = new SavingsPage();
    }

    @Test(priority = 1, groups = {"savings", "ui"}, description = "Verify Search input is available")
    public void test01_SearchInputIsDisplayed() {
        Assert.assertTrue(savingsPage.isSearchInputDisplayed(),
                "Branch locator search input should be visible.");
    }

    @Test(priority = 2, groups = {"savings", "ui"}, description = "Verify Search input placeholder contains text")
    public void test02_SearchInputPlaceholderText() {
        String placeholder = savingsPage.getSearchInputPlaceholder();
        Assert.assertNotNull(placeholder, "Placeholder should not be null.");
        Assert.assertTrue(placeholder.length() > 0, "Placeholder should not be empty.");
    }

    @Test(priority = 3, groups = {"savings", "rgba"}, description = "Verify Search input background RGBA color")
    public void test03_SearchInputBackgroundColorRgba() {
        String bgColor = savingsPage.getSearchInputBackgroundColor();
        log.info("Search input background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Search input background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 4, groups = {"savings", "rgba"}, description = "Verify Search input text RGBA color")
    public void test04_SearchInputTextColorRgba() {
        String textColor = savingsPage.getSearchInputTextColor();
        log.info("Search input text RGBA: {}", textColor);
        Assert.assertTrue(textColor.startsWith("rgb") || textColor.startsWith("rgba"),
                "Search input text color should be in rgb/rgba format. Found: " + textColor);
    }

    @Test(priority = 5, groups = {"savings", "rgba"}, description = "Verify Search button background RGBA color")
    public void test05_SearchButtonBackgroundColorRgba() {
        String bgColor = savingsPage.getSearchButtonBackgroundColor();
        log.info("Search button background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Search button background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 6, groups = {"savings", "rgba"}, description = "Verify Search button text RGBA color")
    public void test06_SearchButtonTextColorRgba() {
        String textColor = savingsPage.getSearchButtonTextColor();
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

    @Test(priority = 7, dataProvider = "irishLocations", groups = {"savings", "functional"}, description = "Verify branch search across Ireland")
    public void test07_SearchBranchesAcrossIreland(String location) {
        savingsPage.searchLocation(location);
        int count = savingsPage.getResultsCount();
        log.info("Location '{}' returned {} results", location, count);
        Assert.assertTrue(count > 0, "Expected at least 1 branch for: " + location);
    }
}
