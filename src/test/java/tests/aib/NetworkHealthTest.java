package tests.aib;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import support.BaseTest;
import support.ConfigReader;
import support.DriverManager;
import support.NetworkHealthUtils;

@Epic("Digital Experience & Infrastructure Integrity")
@Feature("Hybrid API & UI Asset Health Verification")
public class NetworkHealthTest extends BaseTest {

    @Test(groups = {"network", "crawler", "regression"}, priority = 1)
    @Story("Homepage Hyperlink Integrity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Extract and concurrently validate HTTP status codes of AIB homepage hyperlinks via Java 11 HttpClient")
    public void testHomepageNavigationLinksHealth() {
        WebDriver driver = DriverManager.getDriver();
        navigateTo(ConfigReader.get("baseUrl"));

        NetworkHealthUtils.CrawlReport report = NetworkHealthUtils.auditAllPageLinks(driver, 20);
        log.info("Homepage link audit result: {} valid/live, {} broken out of {}",
                report.getValidLinks().size(), report.getBrokenLinks().size(), report.getTotalFound());

        Assert.assertTrue(report.getTotalFound() > 5, "Homepage should contain navigable links");
        Assert.assertTrue(report.getValidLinks().size() >= (report.getTotalFound() * 0.85),
                "At least 85% of audited links must be active and returning valid HTTP responses");
    }

    @Test(groups = {"network", "crawler", "regression"}, priority = 2)
    @Story("Image Asset Renderability & HTTP Status")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate image elements on AIB Homepage for zero broken image sources and valid naturalWidth render dimensions")
    public void testHomepageBrandImageAssetsHealth() {
        WebDriver driver = DriverManager.getDriver();
        navigateTo(ConfigReader.get("baseUrl"));

        NetworkHealthUtils.CrawlReport report = NetworkHealthUtils.auditPageImages(driver, 20);
        log.info("Homepage image asset audit: {} rendered/valid, {} broken out of {}",
                report.getValidLinks().size(), report.getBrokenLinks().size(), report.getTotalFound());

        Assert.assertTrue(report.getTotalFound() > 0, "Homepage should contain image assets");
        Assert.assertEquals(report.getBrokenLinks().size(), 0,
                "No broken image assets should exist on AIB Homepage: " + report.getBrokenLinks());
    }

    @Test(groups = {"network", "compliance", "crawler", "regression"}, priority = 3)
    @Story("Regulatory Disclosures & Consumer Protection Links")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify key regulatory disclosure endpoints return active HTTP 200/302 statuses (Central Bank compliance)")
    public void testRegulatoryAndCentralBankLinks() {
        String[] regulatoryUrls = {
                ConfigReader.get("baseUrl") + "/ways-to-bank",
                ConfigReader.get("baseUrl") + "/help-and-guidance",
                ConfigReader.get("baseUrl") + "/our-products"
        };

        for (String url : regulatoryUrls) {
            NetworkHealthUtils.LinkCheckResult result = NetworkHealthUtils.checkUrl(url);
            log.info("Checking regulatory link [{}]: status={}", url, result.getStatusCode());
            Assert.assertTrue(result.isSuccess(),
                    "Regulatory link must be reachable without error: " + url + " [Code: " + result.getStatusCode() + "]");
        }
    }
}
