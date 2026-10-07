package support;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import io.qameta.allure.Attachment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enterprise Accessibility Auditing Utility powered by Deque axe-core.
 * Enforces WCAG 2.1 Level AA compliance (aligning with European Accessibility Act 2025).
 */
public final class AccessibilityUtils {

    private static final Logger log = LoggerFactory.getLogger(AccessibilityUtils.class);

    public static final List<String> WCAG_21_AA_TAGS = Collections.unmodifiableList(
            Arrays.asList("wcag2a", "wcag2aa", "wcag21a", "wcag21aa")
    );

    private AccessibilityUtils() {
        // Utility class
    }

    /**
     * Executes a full-page WCAG 2.1 AA accessibility audit.
     *
     * @param driver Active WebDriver session
     * @return Axe Results containing violations, passes, and incomplete checks
     */
    public static Results scanPage(WebDriver driver) {
        log.info("Starting WCAG 2.1 AA accessibility scan for page: {}", driver.getCurrentUrl());
        try {
            Results results = new AxeBuilder()
                    .withTags(WCAG_21_AA_TAGS)
                    .analyze(driver);

            logSummary(results);
            attachAuditReportToAllure(driver.getCurrentUrl(), results);
            return results;
        } catch (Exception e) {
            log.error("Failed to run Axe accessibility scan: {}", e.getMessage(), e);
            throw new RuntimeException("Accessibility audit failed: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a targeted accessibility scan on a specific WebElement component.
     *
     * @param driver Active WebDriver session
     * @param contextElement Container element to audit
     * @return Axe Results for the targeted container
     */
    public static Results scanElement(WebDriver driver, WebElement contextElement) {
        log.info("Starting targeted accessibility scan on element: {}", contextElement.getTagName());
        try {
            Results results = new AxeBuilder()
                    .withTags(WCAG_21_AA_TAGS)
                    .analyze(driver, contextElement);

            logSummary(results);
            return results;
        } catch (Exception e) {
            log.error("Failed to run targeted Axe scan: {}", e.getMessage(), e);
            throw new RuntimeException("Targeted accessibility audit failed: " + e.getMessage(), e);
        }
    }

    /**
     * Filters violations by specific impact levels (e.g. "critical", "serious").
     */
    public static List<Rule> getViolationsByImpact(Results results, String... impacts) {
        List<String> impactList = Arrays.stream(impacts)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        return results.getViolations().stream()
                .filter(rule -> rule.getImpact() != null && impactList.contains(rule.getImpact().toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves only critical blocking accessibility violations.
     */
    public static List<Rule> getCriticalViolations(Results results) {
        return getViolationsByImpact(results, "critical");
    }

    /**
     * Retrieves serious accessibility violations.
     */
    public static List<Rule> getSeriousViolations(Results results) {
        return getViolationsByImpact(results, "serious");
    }

    /**
     * Formats violations into a structured human-readable report string.
     */
    public static String formatViolations(List<Rule> violations) {
        if (violations.isEmpty()) {
            return "No accessibility violations detected. Compliant with WCAG 2.1 AA.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Found %d accessibility violation(s):%n", violations.size()));
        for (int i = 0; i < violations.size(); i++) {
            Rule rule = violations.get(i);
            sb.append(String.format(" [%d] Rule: %s | Impact: %s%n", i + 1, rule.getId(), rule.getImpact()));
            sb.append(String.format("     Description: %s%n", rule.getDescription()));
            sb.append(String.format("     Help URL: %s%n", rule.getHelpUrl()));
            sb.append(String.format("     Affected nodes count: %d%n", rule.getNodes().size()));
        }
        return sb.toString();
    }

    private static void logSummary(Results results) {
        List<Rule> violations = results.getViolations();
        long criticalCount = violations.stream().filter(r -> "critical".equalsIgnoreCase(r.getImpact())).count();
        long seriousCount = violations.stream().filter(r -> "serious".equalsIgnoreCase(r.getImpact())).count();
        long moderateCount = violations.stream().filter(r -> "moderate".equalsIgnoreCase(r.getImpact())).count();
        long minorCount = violations.stream().filter(r -> "minor".equalsIgnoreCase(r.getImpact())).count();

        log.info("Accessibility Audit Completed | Total Violations: {} [Critical: {}, Serious: {}, Moderate: {}, Minor: {}]",
                violations.size(), criticalCount, seriousCount, moderateCount, minorCount);
    }

    @Attachment(value = "WCAG 2.1 AA Audit Report", type = "text/plain")
    public static String attachAuditReportToAllure(String url, Results results) {
        return String.format("AIB WCAG 2.1 Level AA Accessibility Audit%nPage URL: %s%n%s",
                url, formatViolations(results.getViolations()));
    }
}
