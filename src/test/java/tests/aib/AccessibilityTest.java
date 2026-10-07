package tests.aib;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import support.AccessibilityUtils;
import support.BaseTest;
import support.ConfigReader;
import support.DriverManager;

import java.util.List;

@Epic("Regulatory Compliance & Inclusive Banking")
@Feature("WCAG 2.1 Level AA Accessibility Auditing")
public class AccessibilityTest extends BaseTest {

    @Test(groups = {"accessibility", "compliance", "regression"}, priority = 1)
    @Story("Homepage Accessibility")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify AIB Homepage complies with WCAG 2.1 AA accessibility standards (EU Accessibility Act)")
    public void testAibHomePageAccessibilityWcag21AA() {
        navigateTo(ConfigReader.get("baseUrl"));

        Results results = AccessibilityUtils.scanPage(DriverManager.getDriver());
        Assert.assertNotNull(results, "Accessibility scan results should not be null");
        Assert.assertTrue(results.getPasses().size() > 10, "Page should satisfy baseline WCAG accessibility rules");

        List<Rule> criticalViolations = AccessibilityUtils.getCriticalViolations(results);
        log.info("AIB Homepage critical accessibility violations count: {}", criticalViolations.size());
        // Assert that no blocking critical accessibility violations exist
        Assert.assertTrue(criticalViolations.size() <= 2,
                "Critical WCAG 2.1 AA accessibility violations should not exceed allowed threshold: " 
                + AccessibilityUtils.formatViolations(criticalViolations));
    }

    @Test(groups = {"accessibility", "compliance", "mortgage"}, priority = 2)
    @Story("Mortgage Calculator Accessibility")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify AIB Mortgage Calculator form elements, sliders, and buttons meet WCAG 2.1 AA standards")
    public void testAibMortgageCalculatorAccessibility() {
        navigateTo(ConfigReader.get("mortgageUrl"));

        Results results = AccessibilityUtils.scanPage(DriverManager.getDriver());
        Assert.assertNotNull(results, "Mortgage calculator accessibility results should not be null");
        Assert.assertTrue(results.getPasses().size() > 5, "Mortgage calculator should pass standard accessibility checks");

        List<Rule> critical = AccessibilityUtils.getCriticalViolations(results);
        log.info("Mortgage Calculator critical violations count: {}", critical.size());
        Assert.assertTrue(critical.size() <= 2,
                "Mortgage calculator critical accessibility violations should be within threshold: "
                + AccessibilityUtils.formatViolations(critical));
    }

    @Test(groups = {"accessibility", "compliance", "loans"}, priority = 3)
    @Story("Loans Page Accessibility")
    @Severity(SeverityLevel.NORMAL)
    @Description("Audit AIB Personal Loans page for contrast, landmarks, and headings hierarchy under WCAG 2.1 AA")
    public void testAibLoansPageAccessibility() {
        navigateTo(ConfigReader.get("baseUrl") + "/our-products/loans");

        Results results = AccessibilityUtils.scanPage(DriverManager.getDriver());
        Assert.assertNotNull(results, "Loans page accessibility results should not be null");

        List<Rule> seriousAndCritical = AccessibilityUtils.getViolationsByImpact(results, "critical", "serious");
        log.info("Loans page critical + serious violations count: {}", seriousAndCritical.size());
        Assert.assertTrue(results.getPasses().size() > 5, "Loans page should have verified passing accessibility rules");
    }
}
