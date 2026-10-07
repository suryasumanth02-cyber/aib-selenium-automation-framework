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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("AIB Digital Banking Web Platform")
@Feature("Mortgage Borrowing Calculator & RGBA Visual Testing")
public class MortgageCalculatorTest extends BaseTest {

    private MortgageCalculatorPage calculatorPage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String url = ConfigReader.get("mortgageUrl", "https://mymortgage.aib.ie/mortgages/calculator");
        navigateTo(url);
        calculatorPage = new MortgageCalculatorPage();
    }

    @Test(priority = 1, description = "Verify Mortgage Calculator page title")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Validates that MyMortgage page title contains proper mortgage identifiers.")
    public void test01_IntroPageTitleIsValid() {
        String title = calculatorPage.getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("mortgage"),
                "Title should contain 'mortgage'. Found: " + title);
    }

    @Test(priority = 2, description = "Verify CTA button is displayed on intro screen")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Ensures 'How much can I borrow?' CTA button is rendered on the calculator intro.")
    public void test02_IntroCtaButtonIsDisplayed() {
        Assert.assertTrue(calculatorPage.isIntroPageLoaded(),
                "The introductory 'How much can I borrow?' CTA button should be displayed.");
    }

    @Test(priority = 3, description = "Verify Intro CTA button background RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that the CTA button background styling is a valid RGBA color.")
    public void test03_IntroButtonBackgroundColorRgba() {
        String bgColor = calculatorPage.getIntroButtonBackgroundColor();
        log.info("Intro CTA background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Intro button background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 4, description = "Verify Intro CTA button text RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that the CTA button text styling is a valid RGBA color.")
    public void test04_IntroButtonTextColorRgba() {
        String textColor = calculatorPage.getIntroButtonTextColor();
        log.info("Intro CTA text RGBA: {}", textColor);
        Assert.assertTrue(textColor.startsWith("rgb") || textColor.startsWith("rgba"),
                "Intro button text color should be in rgb/rgba format. Found: " + textColor);
    }

    @Test(priority = 5, description = "Verify Intro CTA button font size")
    @Severity(SeverityLevel.MINOR)
    @Description("Validates that Intro button has a valid CSS font size property.")
    public void test05_IntroButtonFontSize() {
        String fontSize = calculatorPage.getIntroButtonFontSize();
        log.info("Intro button font size: {}", fontSize);
        Assert.assertTrue(fontSize.endsWith("px") || fontSize.endsWith("rem"),
                "Font size should end with px or rem. Found: " + fontSize);
    }

    @Test(priority = 6, description = "Verify transition to Step 1 upon clicking CTA")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Clicks 'How much can I borrow?' and verifies transition to applicant questions.")
    public void test06_StartCalculationProceedsToStep1() {
        calculatorPage.startCalculation();
        Assert.assertTrue(calculatorPage.isContinueButtonDisplayed(),
                "Continue button should be displayed after proceeding to Step 1.");
    }

    @Test(priority = 7, description = "Verify Single Applicant option is available on Step 1")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures Single Applicant option is available on Step 1.")
    public void test07_SingleApplicantOptionIsAvailable() {
        Assert.assertTrue(calculatorPage.isSingleApplicantOptionDisplayed(),
                "Single applicant option should be available.");
    }

    @Test(priority = 8, description = "Verify Joint Applicant option is available on Step 1")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures Joint Applicant option is available on Step 1.")
    public void test08_JointApplicantOptionIsAvailable() {
        Assert.assertTrue(calculatorPage.isJointApplicantOptionDisplayed(),
                "Joint applicant option should be available.");
    }

    @Test(priority = 9, description = "Verify Single Applicant radio can be selected")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Selects Single Applicant and verifies that radio element is checked.")
    public void test09_SelectSingleApplicantSelectionState() {
        calculatorPage.selectSingleApplicant();
        Assert.assertTrue(calculatorPage.isSingleApplicantSelected(),
                "Single applicant option should be selected.");
    }

    @Test(priority = 10, description = "Verify Joint Applicant radio can be selected")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Selects Joint Applicant and verifies that radio element is checked.")
    public void test10_SelectJointApplicantSelectionState() {
        calculatorPage.selectJointApplicant();
        Assert.assertTrue(calculatorPage.isJointApplicantSelected(),
                "Joint applicant option should be selected.");
    }

    @Test(priority = 11, description = "Verify Continue button is displayed on Step 1")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures the Continue button is displayed on applicant selection screen.")
    public void test11_ContinueButtonIsDisplayed() {
        Assert.assertTrue(calculatorPage.isContinueButtonDisplayed(),
                "Continue button should be visible on Step 1.");
    }

    @Test(priority = 12, description = "Verify Continue button background RGBA color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that Continue button background color is formatted as valid RGBA.")
    public void test12_ContinueButtonBackgroundColorRgba() {
        String bgColor = calculatorPage.getContinueButtonBackgroundColor();
        log.info("Continue button background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Continue button background should be in rgb/rgba format. Found: " + bgColor);
    }
}
