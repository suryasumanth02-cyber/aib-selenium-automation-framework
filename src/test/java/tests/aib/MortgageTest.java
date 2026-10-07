package tests.aib;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.MortgagePage;
import support.BaseTest;
import support.ConfigReader;

public class MortgageTest extends BaseTest {

    private MortgagePage mortgagePage;

    @BeforeClass(alwaysRun = true)
    public void setupPage() {
        String url = ConfigReader.get("mortgageUrl", "https://mymortgage.aib.ie/mortgages/calculator");
        navigateTo(url);
        mortgagePage = new MortgagePage();
    }

    @Test(priority = 1, groups = {"mortgage", "ui"}, description = "Verify Mortgage Calculator page title")
    public void test01_IntroPageTitleIsValid() {
        String title = mortgagePage.getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("mortgage"),
                "Title should contain 'mortgage'. Found: " + title);
    }

    @Test(priority = 2, groups = {"mortgage", "ui"}, description = "Verify CTA button is displayed on intro screen")
    public void test02_IntroCtaButtonIsDisplayed() {
        Assert.assertTrue(mortgagePage.isIntroPageLoaded(),
                "The introductory 'How much can I borrow?' CTA button should be displayed.");
    }

    @Test(priority = 3, groups = {"mortgage", "rgba"}, description = "Verify Intro CTA button background RGBA color")
    public void test03_IntroButtonBackgroundColorRgba() {
        String bgColor = mortgagePage.getIntroButtonBackgroundColor();
        log.info("Intro CTA background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Intro button background should be in rgb/rgba format. Found: " + bgColor);
    }

    @Test(priority = 4, groups = {"mortgage", "rgba"}, description = "Verify Intro CTA button text RGBA color")
    public void test04_IntroButtonTextColorRgba() {
        String textColor = mortgagePage.getIntroButtonTextColor();
        log.info("Intro CTA text RGBA: {}", textColor);
        Assert.assertTrue(textColor.startsWith("rgb") || textColor.startsWith("rgba"),
                "Intro button text color should be in rgb/rgba format. Found: " + textColor);
    }

    @Test(priority = 5, groups = {"mortgage", "ui"}, description = "Verify Intro CTA button font size")
    public void test05_IntroButtonFontSize() {
        String fontSize = mortgagePage.getIntroButtonFontSize();
        log.info("Intro button font size: {}", fontSize);
        Assert.assertTrue(fontSize.endsWith("px") || fontSize.endsWith("rem"),
                "Font size should end with px or rem. Found: " + fontSize);
    }

    @Test(priority = 6, groups = {"mortgage", "functional"}, description = "Verify transition to Step 1 upon clicking CTA")
    public void test06_StartCalculationProceedsToStep1() {
        mortgagePage.startCalculation();
        Assert.assertTrue(mortgagePage.isContinueButtonDisplayed(),
                "Continue button should be displayed after proceeding to Step 1.");
    }

    @Test(priority = 7, groups = {"mortgage", "ui"}, description = "Verify Single Applicant option is available on Step 1")
    public void test07_SingleApplicantOptionIsAvailable() {
        Assert.assertTrue(mortgagePage.isSingleApplicantOptionDisplayed(),
                "Single applicant option should be available.");
    }

    @Test(priority = 8, groups = {"mortgage", "ui"}, description = "Verify Joint Applicant option is available on Step 1")
    public void test08_JointApplicantOptionIsAvailable() {
        Assert.assertTrue(mortgagePage.isJointApplicantOptionDisplayed(),
                "Joint applicant option should be available.");
    }

    @Test(priority = 9, groups = {"mortgage", "functional"}, description = "Verify Single Applicant radio can be selected")
    public void test09_SelectSingleApplicantSelectionState() {
        mortgagePage.selectSingleApplicant();
        Assert.assertTrue(mortgagePage.isSingleApplicantSelected(),
                "Single applicant option should be selected.");
    }

    @Test(priority = 10, groups = {"mortgage", "functional"}, description = "Verify Joint Applicant radio can be selected")
    public void test10_SelectJointApplicantSelectionState() {
        mortgagePage.selectJointApplicant();
        Assert.assertTrue(mortgagePage.isJointApplicantSelected(),
                "Joint applicant option should be selected.");
    }

    @Test(priority = 11, groups = {"mortgage", "ui"}, description = "Verify Continue button is displayed on Step 1")
    public void test11_ContinueButtonIsDisplayed() {
        Assert.assertTrue(mortgagePage.isContinueButtonDisplayed(),
                "Continue button should be visible on Step 1.");
    }

    @Test(priority = 12, groups = {"mortgage", "rgba"}, description = "Verify Continue button background RGBA color")
    public void test12_ContinueButtonBackgroundColorRgba() {
        String bgColor = mortgagePage.getContinueButtonBackgroundColor();
        log.info("Continue button background RGBA: {}", bgColor);
        Assert.assertTrue(bgColor.startsWith("rgb") || bgColor.startsWith("rgba"),
                "Continue button background should be in rgb/rgba format. Found: " + bgColor);
    }
}
