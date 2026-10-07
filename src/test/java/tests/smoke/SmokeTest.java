package tests.smoke;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AibHomePage;
import support.BaseTest;
import support.ConfigReader;

public class SmokeTest extends BaseTest {

    private AibHomePage homePage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String baseUrl = ConfigReader.get("baseUrl", "https://aib.ie");
        navigateTo(baseUrl);
        homePage = new AibHomePage();
    }

    @Test(priority = 1, groups = {"smoke"}, description = "Smoke test: verify AIB portal is alive")
    public void testAibPortalSanity() {
        Assert.assertTrue(homePage.isHeaderDisplayed(), "AIB Header should be visible.");
        Assert.assertTrue(homePage.getPageTitle().toLowerCase().contains("aib"), "Title should contain AIB.");
    }
}
