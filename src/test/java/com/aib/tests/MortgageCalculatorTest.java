package com.aib.tests;

import com.aib.base.BaseTest;
import com.aib.config.ConfigReader;
import com.aib.pages.MortgageCalculatorPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Mortgage Borrowing Calculator")
public class MortgageCalculatorTest extends BaseTest {

    @Test(priority = 1, description = "Verify Mortgage Calculator introductory page loads properly")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verifies that the online mortgage calculation tool loads and the entry action is available.")
    public void testMortgageCalculatorEntry() {
        String url = ConfigReader.get("mortgageUrl", "https://mymortgage.aib.ie/mortgages/calculator");
        navigateTo(url);

        MortgageCalculatorPage calculatorPage = new MortgageCalculatorPage();
        Assert.assertTrue(calculatorPage.isIntroPageLoaded(),
                "The introductory 'How much can I borrow?' CTA button should be displayed.");
    }

    @DataProvider(name = "applicantTypeData")
    public Object[][] getApplicantTypeData() {
        return new Object[][]{
                {1, "Single Applicant"},
                {2, "Joint Applicants"}
        };
    }

    @Test(priority = 2, dataProvider = "applicantTypeData", description = "Verify applicant count selection (Single vs Joint)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Data-driven test verifying applicant count radio button selections in the mortgage tool.")
    public void testApplicantSelection(int applicantCount, String scenarioDescription) {
        log.info("Executing test scenario: {}", scenarioDescription);
        String url = ConfigReader.get("mortgageUrl", "https://mymortgage.aib.ie/mortgages/calculator");
        navigateTo(url);

        MortgageCalculatorPage calculatorPage = new MortgageCalculatorPage();
        calculatorPage.startCalculation();

        if (applicantCount == 1) {
            calculatorPage.selectSingleApplicant();
            Assert.assertTrue(calculatorPage.isSingleApplicantSelected(),
                    "Single applicant option should be selected.");
        } else {
            calculatorPage.selectJointApplicant();
        }

        calculatorPage.proceedToNextStep();
        Assert.assertTrue(calculatorPage.isToolbarDisplayed(),
                "Toolbar navigation should remain active during multi-step application.");
    }
}
