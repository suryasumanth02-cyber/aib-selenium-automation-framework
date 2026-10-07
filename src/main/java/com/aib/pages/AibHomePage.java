package com.aib.pages;

import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

/**
 * Page Object representing the AIB Public Homepage (aib.ie).
 */
public class AibHomePage extends BasePage {

    // Locators
    private final By aibLogo = By.xpath("//a[contains(@class, 'logo') or @aria-label='AIB' or contains(@href, 'aib.ie')]//img | //header//a//img");
    private final By searchButton = By.xpath("//button[contains(@class, 'search') or contains(@aria-label, 'Search') or contains(@id, 'search')]");
    private final By mortgagesNavLink = By.xpath("//a[contains(text(), 'Mortgages') or contains(@href, 'mortgages')]");
    private final By loansNavLink = By.xpath("//a[contains(text(), 'Loans') or contains(@href, 'loans')]");
    private final By everydayBankingNavLink = By.xpath("//a[contains(text(), 'Everyday Banking') or contains(@href, 'everyday-banking')]");
    private final By branchLocatorLink = By.xpath("//a[contains(text(), 'Branch Locator') or contains(@href, 'branch-locator')]");
    private final By pageHeader = By.tagName("header");

    @Step("Verifying that AIB homepage header is loaded")
    public boolean isHeaderDisplayed() {
        return isDisplayed(pageHeader);
    }

    @Step("Verifying that AIB logo is visible")
    public boolean isLogoDisplayed() {
        return isDisplayed(aibLogo);
    }

    @Step("Navigating to Mortgages section")
    public void clickMortgages() {
        click(mortgagesNavLink);
    }

    @Step("Navigating to Loans section")
    public void clickLoans() {
        click(loansNavLink);
    }

    @Step("Clicking Branch Locator link")
    public void clickBranchLocator() {
        click(branchLocatorLink);
    }
}
