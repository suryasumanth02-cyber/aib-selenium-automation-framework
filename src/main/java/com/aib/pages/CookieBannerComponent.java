package com.aib.pages;

import com.aib.constants.FrameworkConstants;
import com.aib.driver.DriverManager;
import com.aib.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Component handling GDPR / OneTrust Cookie Consent banners across AIB web pages.
 */
public class CookieBannerComponent extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CookieBannerComponent.class);

    // Common OneTrust & Irish banking cookie banner locators
    private final By acceptAllButton = By.id("onetrust-accept-btn-handler");
    private final By cookieBannerContainer = By.id("onetrust-banner-sdk");
    private final By fallbackAcceptButton = By.xpath(
            "//button[contains(translate(text(), 'ACCEPT', 'accept'), 'accept all') or contains(text(), 'Accept All') or contains(text(), 'Agree') or contains(text(), 'I Accept')]"
    );

    @Step("Handling GDPR Cookie Consent banner if displayed")
    public void acceptCookiesIfPresent() {
        try {
            log.info("Checking for cookie consent banner presence...");
            WebElement acceptBtn = null;

            try {
                acceptBtn = WaitUtils.waitForClickability(acceptAllButton, Duration.ofSeconds(6));
            } catch (Exception ignored) {
                // Try fallback text-based locator
                try {
                    acceptBtn = WaitUtils.waitForClickability(fallbackAcceptButton, Duration.ofSeconds(3));
                } catch (Exception ignoredAgain) {
                    log.info("No cookie consent modal detected. Proceeding with test.");
                    return;
                }
            }

            if (acceptBtn != null && acceptBtn.isDisplayed()) {
                acceptBtn.click();
                log.info("Successfully accepted cookie consent banner.");
                // Wait briefly for banner to animate away
                WaitUtils.waitForInvisibility(cookieBannerContainer, Duration.ofSeconds(4));
            }
        } catch (Exception e) {
            log.debug("Cookie banner handling skipped or not needed: {}", e.getMessage());
        }
    }
}
