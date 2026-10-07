package com.aib.tests;

import com.aib.base.BaseTest;
import com.aib.config.ConfigReader;
import com.aib.pages.AibHomePage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Homepage Navigation, Branding & RGBA Visual Testing")
public class AibNavigationTest extends BaseTest {

    private AibHomePage homePage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String baseUrl = ConfigReader.get("baseUrl", "https://aib.ie");
        navigateTo(baseUrl);
        homePage = new AibHomePage();
    }

    @Test(priority = 1, description = "Verify that AIB homepage header is visible")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that the main page header container is rendered on the homepage.")
    public void test01_HeaderIsDisplayed() {
        Assert.assertTrue(homePage.isHeaderDisplayed(), "Header should be visible on homepage.");
    }

    @Test(priority = 2, description = "Verify that AIB logo is visible")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that the official AIB brand logo is rendered.")
    public void test02_AibLogoIsDisplayed() {
        Assert.assertTrue(homePage.isLogoDisplayed(), "AIB logo should be visible.");
    }

    @Test(priority = 3, description = "Verify AIB logo image source is valid")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that the logo image tag contains a non-empty image source attribute.")
    public void test03_LogoSrcAttributeIsValid() {
        String src = homePage.getLogoSrc();
        Assert.assertNotNull(src, "Logo src attribute should not be null.");
        Assert.assertTrue(src.length() > 0, "Logo src attribute should not be empty.");
    }

    @Test(priority = 4, description = "Verify page title branding")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Ensures that the page title contains 'AIB' brand keywords.")
    public void test04_PageTitleContainsBranding() {
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("aib"),
                "Page title should contain 'AIB'. Found: " + title);
    }

    @Test(priority = 5, description = "Verify Header background RGBA color formatting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that header CSS background-color returns a valid RGB/RGBA color string.")
    public void test05_HeaderBackgroundColorRgba() {
        String bgColor = homePage.getHeaderBackgroundColor();
        log.info("Header background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Header background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 6, description = "Verify Mortgages menu font RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that Mortgages navigation item text is styled with a valid RGBA color.")
    public void test06_MortgagesNavTextColorRgba() {
        String color = homePage.getMortgagesNavTextColor();
        log.info("Mortgages nav text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Mortgages text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 7, description = "Verify Mortgages navigation font size")
    @Severity(SeverityLevel.MINOR)
    @Description("Validates that Mortgages menu item has an appropriate font size configured.")
    public void test07_MortgagesNavFontSize() {
        String fontSize = homePage.getMortgagesNavFontSize();
        log.info("Mortgages nav font-size: {}", fontSize);
        Assert.assertTrue(fontSize.endsWith("px") || fontSize.endsWith("rem"),
                "Font size should end with px or rem. Found: " + fontSize);
    }

    @Test(priority = 8, description = "Verify Products navigation button presence")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that the Products navigation button exists in the primary header.")
    public void test08_ProductsNavButtonDisplayed() {
        Assert.assertTrue(homePage.isProductsNavButtonDisplayed(), "Products button should be displayed.");
    }

    @Test(priority = 9, description = "Verify Products button text RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that Products navigation button is styled with valid RGBA color.")
    public void test09_ProductsNavTextColorRgba() {
        String color = homePage.getProductsNavTextColor();
        log.info("Products button text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Products text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 10, description = "Verify Ways to Bank navigation button presence")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that Ways to Bank navigation button is rendered.")
    public void test10_WaysToBankNavButtonDisplayed() {
        Assert.assertTrue(homePage.isWaysToBankNavButtonDisplayed(),
                "Ways to Bank button should be displayed.");
    }

    @Test(priority = 11, description = "Verify Ways to Bank font RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that Ways to Bank navigation text is styled with valid RGBA color.")
    public void test11_WaysToBankTextColorRgba() {
        String color = homePage.getWaysToBankTextColor();
        log.info("Ways to Bank text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Ways to Bank text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 12, description = "Verify Help & Guidance navigation button presence")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that Help & Guidance navigation button is rendered.")
    public void test12_HelpAndGuidanceNavButtonDisplayed() {
        Assert.assertTrue(homePage.isHelpAndGuidanceNavButtonDisplayed(),
                "Help & Guidance button should be displayed.");
    }
}
