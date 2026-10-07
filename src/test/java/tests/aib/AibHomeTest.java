package tests.aib;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AibHomePage;
import support.BaseTest;
import support.ConfigReader;

public class AibHomeTest extends BaseTest {

    private AibHomePage homePage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String baseUrl = ConfigReader.get("baseUrl", "https://aib.ie");
        navigateTo(baseUrl);
        homePage = new AibHomePage();
    }

    @Test(priority = 1, groups = {"home", "ui"}, description = "Verify that AIB homepage header is visible")
    public void test01_HeaderIsDisplayed() {
        Assert.assertTrue(homePage.isHeaderDisplayed(), "Header should be visible on homepage.");
    }

    @Test(priority = 2, groups = {"home", "ui"}, description = "Verify that AIB logo is visible")
    public void test02_AibLogoIsDisplayed() {
        Assert.assertTrue(homePage.isLogoDisplayed(), "AIB logo should be visible.");
    }

    @Test(priority = 3, groups = {"home", "ui"}, description = "Verify AIB logo image source is valid")
    public void test03_LogoSrcAttributeIsValid() {
        String src = homePage.getLogoSrc();
        Assert.assertNotNull(src, "Logo src attribute should not be null.");
        Assert.assertTrue(src.length() > 0, "Logo src attribute should not be empty.");
    }

    @Test(priority = 4, groups = {"home", "ui"}, description = "Verify page title branding")
    public void test04_PageTitleContainsBranding() {
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("aib"),
                "Page title should contain 'AIB'. Found: " + title);
    }

    @Test(priority = 5, groups = {"home", "rgba"}, description = "Verify Header background RGBA color formatting")
    public void test05_HeaderBackgroundColorRgba() {
        String bgColor = homePage.getHeaderBackgroundColor();
        log.info("Header background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Header background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 6, groups = {"home", "rgba"}, description = "Verify Mortgages menu font RGBA color")
    public void test06_MortgagesNavTextColorRgba() {
        String color = homePage.getMortgagesNavTextColor();
        log.info("Mortgages nav text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Mortgages text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 7, groups = {"home", "ui"}, description = "Verify Mortgages navigation font size")
    public void test07_MortgagesNavFontSize() {
        String fontSize = homePage.getMortgagesNavFontSize();
        log.info("Mortgages nav font-size: {}", fontSize);
        Assert.assertTrue(fontSize.endsWith("px") || fontSize.endsWith("rem"),
                "Font size should end with px or rem. Found: " + fontSize);
    }

    @Test(priority = 8, groups = {"home", "ui"}, description = "Verify Products navigation button presence")
    public void test08_ProductsNavButtonDisplayed() {
        Assert.assertTrue(homePage.isProductsNavButtonDisplayed(), "Products button should be displayed.");
    }

    @Test(priority = 9, groups = {"home", "rgba"}, description = "Verify Products button text RGBA color")
    public void test09_ProductsNavTextColorRgba() {
        String color = homePage.getProductsNavTextColor();
        log.info("Products button text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Products text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 10, groups = {"home", "ui"}, description = "Verify Ways to Bank navigation button presence")
    public void test10_WaysToBankNavButtonDisplayed() {
        Assert.assertTrue(homePage.isWaysToBankNavButtonDisplayed(),
                "Ways to Bank button should be displayed.");
    }

    @Test(priority = 11, groups = {"home", "rgba"}, description = "Verify Ways to Bank font RGBA color")
    public void test11_WaysToBankTextColorRgba() {
        String color = homePage.getWaysToBankTextColor();
        log.info("Ways to Bank text RGBA: {}", color);
        Assert.assertTrue(color.startsWith("rgb") || color.startsWith("rgba"),
                "Ways to Bank text color should be in rgb/rgba format. Found: " + color);
    }

    @Test(priority = 12, groups = {"home", "ui"}, description = "Verify Help & Guidance navigation button presence")
    public void test12_HelpAndGuidanceNavButtonDisplayed() {
        Assert.assertTrue(homePage.isHelpAndGuidanceNavButtonDisplayed(),
                "Help & Guidance button should be displayed.");
    }
}
