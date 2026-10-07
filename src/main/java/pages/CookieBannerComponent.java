package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import support.WaitUtils;

import java.time.Duration;

public class CookieBannerComponent extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CookieBannerComponent.class);

    private final By acceptAllButton = By.id("onetrust-accept-btn-handler");
    private final By cookieBannerContainer = By.id("onetrust-banner-sdk");
    private final By fallbackAcceptButton = By.xpath(
            "//button[contains(translate(text(), 'ACCEPT', 'accept'), 'accept all') or contains(text(), 'Accept All') or contains(text(), 'Agree') or contains(text(), 'I Accept')]"
    );

    public void acceptCookiesIfPresent() {
        try {
            log.info("Checking for cookie consent banner...");
            WebElement acceptBtn = null;

            try {
                acceptBtn = WaitUtils.waitForClickability(acceptAllButton, Duration.ofSeconds(5));
            } catch (Exception ignored) {
                try {
                    acceptBtn = WaitUtils.waitForClickability(fallbackAcceptButton, Duration.ofSeconds(2));
                } catch (Exception ignoredAgain) {
                    return;
                }
            }

            if (acceptBtn != null && acceptBtn.isDisplayed()) {
                acceptBtn.click();
                log.info("Accepted GDPR cookie banner.");
                WaitUtils.waitForInvisibility(cookieBannerContainer, Duration.ofSeconds(4));
            }
        } catch (Exception ignored) {}
    }
}
