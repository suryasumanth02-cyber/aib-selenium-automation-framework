package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import support.WaitUtils;

import java.time.Duration;
import java.util.List;

public class SavingsPage extends BasePage {

    private final By searchInput = By.xpath("//input[contains(@class, 'yxt-SearchBar-input') or @name='query' or contains(@placeholder, 'search') or @type='text']");
    private final By searchSubmitButton = By.xpath("//button[contains(@class, 'yxt-SearchBar-button') or @type='submit']");
    private final By locationCards = By.xpath("//div[contains(@class, 'LocationCard') or contains(@class, 'result') or contains(@class, 'c-location-card')] | //article");

    public boolean isSearchInputDisplayed() {
        return isDisplayed(searchInput);
    }

    public String getSearchInputPlaceholder() {
        return getAttribute(searchInput, "placeholder");
    }

    public String getSearchInputBackgroundColor() {
        return getCssValue(searchInput, "background-color");
    }

    public String getSearchInputTextColor() {
        return getCssValue(searchInput, "color");
    }

    public String getSearchButtonBackgroundColor() {
        return getCssValue(searchSubmitButton, "background-color");
    }

    public String getSearchButtonTextColor() {
        return getCssValue(searchSubmitButton, "color");
    }

    public SavingsPage searchLocation(String location) {
        WebElement input = WaitUtils.waitForVisibility(searchInput);
        input.clear();
        input.sendKeys(location);
        input.sendKeys(Keys.ENTER);
        return this;
    }

    public int getResultsCount() {
        try {
            List<WebElement> cards = WaitUtils.waitForAllVisible(locationCards, Duration.ofSeconds(8));
            return cards.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
