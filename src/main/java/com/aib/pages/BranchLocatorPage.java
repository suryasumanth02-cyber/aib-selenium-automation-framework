package com.aib.pages;

import com.aib.utils.JavaScriptUtils;
import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for AIB Branch & ATM Locator (https://branches.aib.ie/search).
 */
public class BranchLocatorPage extends BasePage {

    // Locators
    private final By searchInput = By.xpath("//input[contains(@class, 'yxt-SearchBar-input') or @name='query' or contains(@placeholder, 'search') or @type='text']");
    private final By searchSubmitButton = By.xpath("//button[contains(@class, 'yxt-SearchBar-button') or @type='submit']");
    private final By locationCards = By.xpath("//div[contains(@class, 'LocationCard') or contains(@class, 'result') or contains(@class, 'c-location-card')] | //article");
    private final By resultsListContainer = By.xpath("//div[contains(@class, 'ResultList') or contains(@class, 'results') or contains(@class, 'cards')]");

    @Step("Verifying that the Branch Locator search input is displayed")
    public boolean isSearchInputDisplayed() {
        return isDisplayed(searchInput);
    }

    @Step("Searching for branches in location: {location}")
    public BranchLocatorPage searchLocation(String location) {
        log.info("Searching for branches in '{}'", location);
        WebElement input = WaitUtils.waitForVisibility(searchInput);
        input.clear();
        input.sendKeys(location);
        input.sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Retrieving count of branch results found")
    public int getResultsCount() {
        try {
            List<WebElement> cards = WaitUtils.waitForAllVisible(locationCards, Duration.ofSeconds(10));
            log.info("Found {} branch cards in search results", cards.size());
            return cards.size();
        } catch (Exception e) {
            log.warn("No location cards appeared within timeout: {}", e.getMessage());
            return 0;
        }
    }
}
