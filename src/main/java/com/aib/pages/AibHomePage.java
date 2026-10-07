package com.aib.pages;

import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object representing the AIB Public Homepage (aib.ie) with styling/RGBA inspection.
 */
public class AibHomePage extends BasePage {

    // Locators for AIB's top-level header elements
    private final By aibLogo = By.xpath("//a[contains(@class, 'logo') or @aria-label='AIB' or contains(@href, 'aib.ie')]//img | //header//a//img");
    private final By pageHeader = By.tagName("header");
    private final By mortgagesNavButton = By.xpath("//button[contains(text(), 'Mortgages')] | //a[contains(text(), 'Mortgages')]");
    private final By productsNavButton = By.xpath("//button[contains(text(), 'Products')] | //a[contains(text(), 'Products')]");
    private final By waysToBankNavButton = By.xpath("//button[contains(text(), 'Ways to bank')] | //a[contains(text(), 'Ways to bank')]");
    private final By helpAndGuidanceNavButton = By.xpath("//button[contains(text(), 'Help and guidance')] | //a[contains(text(), 'Help and guidance')]");
    private final By makeAPlanNavButton = By.xpath("//button[contains(text(), 'Make a plan')] | //a[contains(text(), 'Make a plan')]");
    private final By iWantToNavButton = By.xpath("//button[contains(text(), 'I want to')]");

    @Step("Verifying that AIB homepage header is loaded")
    public boolean isHeaderDisplayed() {
        return isDisplayed(pageHeader);
    }

    @Step("Verifying that AIB logo is visible")
    public boolean isLogoDisplayed() {
        return isDisplayed(aibLogo);
    }

    @Step("Retrieving logo source URL")
    public String getLogoSrc() {
        return getAttribute(aibLogo, "src");
    }

    @Step("Retrieving header background color RGBA")
    public String getHeaderBackgroundColor() {
        return getCssValue(pageHeader, "background-color");
    }

    @Step("Retrieving Mortgages nav item text color RGBA")
    public String getMortgagesNavTextColor() {
        return getCssValue(mortgagesNavButton, "color");
    }

    @Step("Retrieving Mortgages nav item font size")
    public String getMortgagesNavFontSize() {
        return getCssValue(mortgagesNavButton, "font-size");
    }

    @Step("Verifying Products navigation button is displayed")
    public boolean isProductsNavButtonDisplayed() {
        return isDisplayed(productsNavButton);
    }

    @Step("Retrieving Products nav button text color RGBA")
    public String getProductsNavTextColor() {
        return getCssValue(productsNavButton, "color");
    }

    @Step("Verifying Ways to Bank navigation button is displayed")
    public boolean isWaysToBankNavButtonDisplayed() {
        return isDisplayed(waysToBankNavButton);
    }

    @Step("Retrieving Ways to Bank font color RGBA")
    public String getWaysToBankTextColor() {
        return getCssValue(waysToBankNavButton, "color");
    }

    @Step("Verifying Help and Guidance navigation button is displayed")
    public boolean isHelpAndGuidanceNavButtonDisplayed() {
        return isDisplayed(helpAndGuidanceNavButton);
    }

    @Step("Verifying Make a Plan navigation button is displayed")
    public boolean isMakeAPlanNavButtonDisplayed() {
        return isDisplayed(makeAPlanNavButton);
    }
}
