package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import support.JavaScriptUtils;
import support.WaitUtils;

import java.time.Duration;

public class MortgagePage extends BasePage {

    private final By introProceedButton = By.id("mcx-calculator-intro-proceed");
    private final By singleApplicantRadio = By.id("mcx-calculator-applicant1-applicants-number-value-1");
    private final By jointApplicantRadio = By.id("mcx-calculator-applicant1-applicants-number-value-2");
    private final By singleApplicantLabel = By.xpath("//label[contains(text(), '1') or @for='mcx-calculator-applicant1-applicants-number-value-1']");
    private final By jointApplicantLabel = By.xpath("//label[contains(text(), '2') or @for='mcx-calculator-applicant1-applicants-number-value-2']");
    private final By applicantsProceedButton = By.id("mcx-calculator-applicants-number-proceed");
    private final By toolbarBackButton = By.id("mcx-calculator-toolbar-back");
    private final By toolbarCloseButton = By.id("mcx-calculator-toolbar-close");

    public boolean isIntroPageLoaded() {
        return isDisplayed(introProceedButton);
    }

    public String getIntroButtonTextColor() {
        return getCssValue(introProceedButton, "color");
    }

    public String getIntroButtonBackgroundColor() {
        return getCssValue(introProceedButton, "background-color");
    }

    public String getIntroButtonFontSize() {
        return getCssValue(introProceedButton, "font-size");
    }

    public MortgagePage startCalculation() {
        log.info("Starting mortgage calculation...");
        click(introProceedButton);
        WaitUtils.waitForVisibility(applicantsProceedButton, Duration.ofSeconds(10));
        return this;
    }

    public boolean isSingleApplicantOptionDisplayed() {
        return isDisplayed(singleApplicantLabel) || WaitUtils.waitForPresence(singleApplicantRadio) != null;
    }

    public boolean isJointApplicantOptionDisplayed() {
        return isDisplayed(jointApplicantLabel) || WaitUtils.waitForPresence(jointApplicantRadio) != null;
    }

    public MortgagePage selectSingleApplicant() {
        WebElement radio = WaitUtils.waitForPresence(singleApplicantRadio);
        JavaScriptUtils.clickElement(radio);
        return this;
    }

    public MortgagePage selectJointApplicant() {
        WebElement radio = WaitUtils.waitForPresence(jointApplicantRadio);
        JavaScriptUtils.clickElement(radio);
        return this;
    }

    public boolean isSingleApplicantSelected() {
        WebElement radio = WaitUtils.waitForPresence(singleApplicantRadio);
        return radio.isSelected();
    }

    public boolean isJointApplicantSelected() {
        WebElement radio = WaitUtils.waitForPresence(jointApplicantRadio);
        return radio.isSelected();
    }

    public boolean isContinueButtonDisplayed() {
        return isDisplayed(applicantsProceedButton);
    }

    public String getContinueButtonBackgroundColor() {
        return getCssValue(applicantsProceedButton, "background-color");
    }

    public String getContinueButtonTextColor() {
        return getCssValue(applicantsProceedButton, "color");
    }

    public boolean isToolbarDisplayed() {
        return isDisplayed(toolbarBackButton) || isDisplayed(toolbarCloseButton);
    }
}
