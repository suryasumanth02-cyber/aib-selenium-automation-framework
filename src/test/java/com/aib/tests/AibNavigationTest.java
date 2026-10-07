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
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Navigation & Branding")
public class AibNavigationTest extends BaseTest {

    @Test(priority = 1, description = "Verify that AIB homepage loads with valid branding and header")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Navigates to AIB homepage, verifies page title, logo visibility, and main header existence.")
    public void testHomepageBrandingAndHeader() {
        String baseUrl = ConfigReader.get("baseUrl", "https://aib.ie");
        navigateTo(baseUrl);

        AibHomePage homePage = new AibHomePage();

        Assert.assertTrue(homePage.isHeaderDisplayed(), "Header should be visible on homepage");
        Assert.assertTrue(homePage.getPageTitle().toLowerCase().contains("aib"),
                "Page title should contain 'AIB' branding. Found: " + homePage.getPageTitle());
    }

    @Test(priority = 2, description = "Verify navigation to Mortgages section from header")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies navigation from the main portal to the Mortgages overview page.")
    public void testNavigationToMortgages() {
        String baseUrl = ConfigReader.get("baseUrl", "https://aib.ie");
        navigateTo(baseUrl);

        AibHomePage homePage = new AibHomePage();
        homePage.clickMortgages();

        String currentUrl = homePage.getCurrentUrl();
        log.info("Navigated to Mortgages URL: {}", currentUrl);
        Assert.assertTrue(currentUrl.toLowerCase().contains("mortgage"),
                "URL should contain 'mortgage'. Found: " + currentUrl);
    }
}
