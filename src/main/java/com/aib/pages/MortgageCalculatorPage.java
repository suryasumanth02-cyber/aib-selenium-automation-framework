package com.aib.pages;

import com.aib.utils.JavaScriptUtils;
import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Page Object for AIB's Interactive Mortgage Calculator (https://mymortgage.aib.ie/mortgages/calculator).
 */
public class MortgageCalculatorPage extends BasePage {

    // Locators
    private final By introProceedButton = By.id("mcx-calculator-intro-proceed");
    private final By singleApplicantRadio = By.id("mcx-calculator-applicant1-applicants-number-value-1");
    private final By jointApplicantRadio = By.id("mcx-calculator-applicant1-applicants-number-value-2");
    private final By applicantsProceedButton = By.id("mcx-calculator-applicants-number-proceed");
    private final By toolbarBackButton = By.id("mcx-calculator-toolbar-back");
    private final By toolbarCloseButton = By.id("mcx-calculator-toolbar-close");

    @Step("Verifying that the Mortgage Calculator introductory page is loaded")
    public boolean isIntroPageLoaded() {
        return isDisplayed(introProceedButton);
    }

    @Step("Clicking 'How much can I borrow?' to begin mortgage assessment")
    public MortgageCalculatorPage startCalculation() {
        log.info("Starting mortgage borrowing calculation...");
        click(introProceedButton);
        return this;
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

    @Step("Clicking Continue to proceed to financial details step")
    public MortgageCalculatorPage proceedToNextStep() {
        log.info("Proceeding to next step...");
        click(applicantsProceedButton);
        return this;
    }

    @Step("Verifying toolbar navigation elements are displayed")
    public boolean isToolbarDisplayed() {
        return isDisplayed(toolbarBackButton) || isDisplayed(toolbarCloseButton);
    }
}
