package pages;

import org.openqa.selenium.By;

public class AibHomePage extends BasePage {

    private final By aibLogo = By.xpath("//a[contains(@class, 'logo') or @aria-label='AIB' or contains(@href, 'aib.ie')]//img | //header//a//img");
    private final By pageHeader = By.tagName("header");
    private final By mortgagesNavButton = By.xpath("//button[contains(text(), 'Mortgages')] | //a[contains(text(), 'Mortgages')]");
    private final By productsNavButton = By.xpath("//button[contains(text(), 'Products')] | //a[contains(text(), 'Products')]");
    private final By waysToBankNavButton = By.xpath("//button[contains(text(), 'Ways to bank')] | //a[contains(text(), 'Ways to bank')]");
    private final By helpAndGuidanceNavButton = By.xpath("//button[contains(text(), 'Help and guidance')] | //a[contains(text(), 'Help and guidance')]");
    private final By makeAPlanNavButton = By.xpath("//button[contains(text(), 'Make a plan')] | //a[contains(text(), 'Make a plan')]");

    public boolean isHeaderDisplayed() {
        return isDisplayed(pageHeader);
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(aibLogo);
    }

    public String getLogoSrc() {
        return getAttribute(aibLogo, "src");
    }

    public String getHeaderBackgroundColor() {
        return getCssValue(pageHeader, "background-color");
    }

    public String getMortgagesNavTextColor() {
        return getCssValue(mortgagesNavButton, "color");
    }

    public String getMortgagesNavFontSize() {
        return getCssValue(mortgagesNavButton, "font-size");
    }

    public boolean isProductsNavButtonDisplayed() {
        return isDisplayed(productsNavButton);
    }

    public String getProductsNavTextColor() {
        return getCssValue(productsNavButton, "color");
    }

    public boolean isWaysToBankNavButtonDisplayed() {
        return isDisplayed(waysToBankNavButton);
    }

    public String getWaysToBankTextColor() {
        return getCssValue(waysToBankNavButton, "color");
    }

    public boolean isHelpAndGuidanceNavButtonDisplayed() {
        return isDisplayed(helpAndGuidanceNavButton);
    }

    public boolean isMakeAPlanNavButtonDisplayed() {
        return isDisplayed(makeAPlanNavButton);
    }
}
