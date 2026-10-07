package com.aib.pages;

import com.aib.utils.JavaScriptUtils;
import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Page Object for AIB's Interactive Mortgage Calculator with RGBA and UI validation.
 */
public class MortgageCalculatorPage extends BasePage {

    // Locators
    private final By introProceedButton = By.id("mcx-calculator-intro-proceed");
    private final By singleApplicantRadio = By.id("mcx-calculator-applicant1-applicants-number-value-1");
    private final By jointApplicantRadio = By.id("mcx-calculator-applicant1-applicants-number-value-2");
    private final By singleApplicantLabel = By.xpath("//label[contains(text(), '1') or @for='mcx-calculator-applicant1-applicants-number-value-1']");
    private final By jointApplicantLabel = By.xpath("//label[contains(text(), '2') or @for='mcx-calculator-applicant1-applicants-number-value-2']");
    private final By applicantsProceedButton = By.id("mcx-calculator-applicants-number-proceed");
    private final By toolbarBackButton = By.id("mcx-calculator-toolbar-back");
    private final By toolbarCloseButton = By.id("mcx-calculator-toolbar-close");

    @Step("Verifying that the Mortgage Calculator introductory page is loaded")
    public boolean isIntroPageLoaded() {
        return isDisplayed(introProceedButton);
    }

    @Step("Retrieving Intro button text color RGBA")
    public String getIntroButtonTextColor() {
        return getCssValue(introProceedButton, "color");
    }

    @Step("Retrieving Intro button background color RGBA")
    public String getIntroButtonBackgroundColor() {
        return getCssValue(introProceedButton, "background-color");
    }

    @Step("Retrieving Intro button font size")
    public String getIntroButtonFontSize() {
        return getCssValue(introProceedButton, "font-size");
    }

    @Step("Clicking 'How much can I borrow?' to begin mortgage assessment")
    public MortgageCalculatorPage startCalculation() {
        log.info("Starting mortgage borrowing calculation...");
        click(introProceedButton);
        // Wait for Continue button to appear on Step 1
        WaitUtils.waitForVisibility(applicantsProceedButton, Duration.ofSeconds(10));
        return this;
    }

    @Step("Verifying Single Applicant option is available on Step 1")
    public boolean isSingleApplicantOptionDisplayed() {
        return isDisplayed(singleApplicantLabel) || WaitUtils.waitForPresence(singleApplicantRadio) != null;
    }

    @Step("Verifying Joint Applicant option is available on Step 1")
    public boolean isJointApplicantOptionDisplayed() {
        return isDisplayed(jointApplicantLabel) || WaitUtils.waitForPresence(jointApplicantRadio) != null;
    }

    @Step("Selecting number of applicants: 1 (Single Applicant)")
    public MortgageCalculatorPage selectSingleApplicant() {
        log.info("Selecting single applicant...");
        WebElement radio = WaitUtils.waitForPresence(singleApplicantRadio);
        JavaScriptUtils.clickElement(radio);
        return this;
    }

    @Step("Selecting number of applicants: 2 (Joint Applicants)")
    public MortgageCalculatorPage selectJointApplicant() {
        log.info("Selecting joint applicants...");
        WebElement radio = WaitUtils.waitForPresence(jointApplicantRadio);
        JavaScriptUtils.clickElement(radio);
        return this;
    }

    @Step("Verifying applicant radio button selection state")
    public boolean isSingleApplicantSelected() {
        WebElement radio = WaitUtils.waitForPresence(singleApplicantRadio);
        return radio.isSelected();
    }

    @Step("Verifying joint applicant radio button selection state")
    public boolean isJointApplicantSelected() {
        WebElement radio = WaitUtils.waitForPresence(jointApplicantRadio);
        return radio.isSelected();
    }

    @Step("Verifying Continue button is displayed")
    public boolean isContinueButtonDisplayed() {
        return isDisplayed(applicantsProceedButton);
    }

    @Step("Retrieving Continue button background color RGBA")
    public String getContinueButtonBackgroundColor() {
        return getCssValue(applicantsProceedButton, "background-color");
    }

    @Step("Retrieving Continue button text color RGBA")
    public String getContinueButtonTextColor() {
        return getCssValue(applicantsProceedButton, "color");
    }

    @Step("Verifying toolbar navigation elements are displayed")
    public boolean isToolbarDisplayed() {
        return isDisplayed(toolbarBackButton) || isDisplayed(toolbarCloseButton);
    }
}
