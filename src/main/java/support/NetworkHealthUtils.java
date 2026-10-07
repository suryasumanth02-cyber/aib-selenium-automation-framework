package support;

import io.qameta.allure.Attachment;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * High-performance API & UI Hybrid crawler for validating link and asset health.
 * Employs non-blocking Java 11+ HttpClient and parallel futures to audit links and assets.
 */
public final class NetworkHealthUtils {

    private static final Logger log = LoggerFactory.getLogger(NetworkHealthUtils.class);
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(6))
            .build();

    private NetworkHealthUtils() {
        // Utility class
    }

    public static class LinkCheckResult {
        private final String url;
        private final int statusCode;
        private final String statusMessage;
        private final boolean isSuccess;

        public LinkCheckResult(String url, int statusCode, String statusMessage, boolean isSuccess) {
            this.url = url;
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
            this.isSuccess = isSuccess;
        }

        public String getUrl() { return url; }
        public int getStatusCode() { return statusCode; }
        public String getStatusMessage() { return statusMessage; }
        public boolean isSuccess() { return isSuccess; }

        @Override
        public String toString() {
            return String.format("[%d %s] %s", statusCode, statusMessage, url);
        }
    }

    public static class CrawlReport {
        private final int totalFound;
        private final List<LinkCheckResult> validLinks = new ArrayList<>();
        private final List<LinkCheckResult> brokenLinks = new ArrayList<>();

        public CrawlReport(int totalFound) {
            this.totalFound = totalFound;
        }

        public int getTotalFound() { return totalFound; }
        public List<LinkCheckResult> getValidLinks() { return validLinks; }
        public List<LinkCheckResult> getBrokenLinks() { return brokenLinks; }
        public boolean hasBrokenLinks() { return !brokenLinks.isEmpty(); }
    }

    /**
     * Checks HTTP response status code for a given URL with realistic browser headers.
     * Categorizes 2xx/3xx (Accessible) and 401/403 (Protected/Active Security Gateway) as reachable,
     * while flagging 404/410 (Dead Link) and 5xx (Server Fault) as broken.
     */
    public static LinkCheckResult checkUrl(String url) {
        if (url == null || url.trim().isEmpty() || !url.startsWith("http")) {
            return new LinkCheckResult(url, 0, "SKIPPED_NOT_HTTP", true);
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Sec-Ch-Ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\"")
                    .header("Sec-Ch-Ua-Mobile", "?0")
                    .header("Sec-Ch-Ua-Platform", "\"Windows\"")
                    .timeout(Duration.ofSeconds(8))
                    .GET()
                    .build();

            HttpResponse<Void> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());
            int code = response.statusCode();

            // 2xx, 3xx, 401, 403 indicate endpoint is alive and served (not a 404/5xx broken link)
            boolean ok = (code >= 200 && code < 400) || code == 403 || code == 401;
            String msg = (code >= 200 && code < 400) ? "OK" : (code == 403 || code == 401 ? "PROTECTED_LIVE" : "DEAD_OR_ERROR");

            return new LinkCheckResult(url, code, msg, ok);
        } catch (Exception e) {
            return new LinkCheckResult(url, 0, e.getClass().getSimpleName() + ": " + e.getMessage(), false);
        }
    }

    /**
     * Extracts and concurrently audits hyperlink URLs on the active page.
     */
    public static CrawlReport auditAllPageLinks(WebDriver driver, int maxLinksToScan) {
        List<WebElement> linkElements = driver.findElements(By.tagName("a"));
        List<String> rawUrls = linkElements.stream()
                .map(el -> {
                    try {
                        return el.getAttribute("href");
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(href -> href != null && href.startsWith("http") && !href.contains("javascript:") && !href.contains("#"))
                .distinct()
                .limit(maxLinksToScan > 0 ? maxLinksToScan : 30)
                .collect(Collectors.toList());

        log.info("Auditing {} unique links via asynchronous HTTP requests...", rawUrls.size());
        CrawlReport report = new CrawlReport(rawUrls.size());

        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            List<CompletableFuture<LinkCheckResult>> futures = rawUrls.stream()
                    .map(url -> CompletableFuture.supplyAsync(() -> checkUrl(url), executor))
                    .collect(Collectors.toList());

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            for (CompletableFuture<LinkCheckResult> future : futures) {
                LinkCheckResult res = future.join();
                if (res.isSuccess()) {
                    report.getValidLinks().add(res);
                } else {
                    report.getBrokenLinks().add(res);
                }
            }
        } finally {
            executor.shutdown();
        }

        log.info("Link audit complete: {} total, {} valid/live, {} broken",
                report.getTotalFound(), report.getValidLinks().size(), report.getBrokenLinks().size());
        attachCrawlReportToAllure("Hyperlink Health Report", report);
        return report;
    }

    /**
     * Audits all <img> elements on the page for both HTTP 200 response and browser render dimensions.
     */
    public static CrawlReport auditPageImages(WebDriver driver, int maxImagesToScan) {
        List<WebElement> imgElements = driver.findElements(By.tagName("img"));
        List<WebElement> limitedImages = imgElements.stream()
                .limit(maxImagesToScan > 0 ? maxImagesToScan : 20)
                .collect(Collectors.toList());

        log.info("Auditing {} image elements for renderability and HTTP status...", limitedImages.size());
        CrawlReport report = new CrawlReport(limitedImages.size());
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (WebElement img : limitedImages) {
            try {
                String src = img.getAttribute("src");
                if (src == null || !src.startsWith("http")) {
                    continue;
                }

                Boolean isRendered = (Boolean) js.executeScript(
                        "return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
                        img
                );

                if (Boolean.TRUE.equals(isRendered)) {
                    report.getValidLinks().add(new LinkCheckResult(src, 200, "RENDERED_OK", true));
                } else {
                    LinkCheckResult netRes = checkUrl(src);
                    if (netRes.isSuccess()) {
                        report.getValidLinks().add(netRes);
                    } else {
                        report.getBrokenLinks().add(netRes);
                    }
                }
            } catch (Exception e) {
                log.warn("Image verification exception: {}", e.getMessage());
            }
        }

        attachCrawlReportToAllure("Image Assets Health Report", report);
        return report;
    }

    @Attachment(value = "{title}", type = "text/plain")
    public static String attachCrawlReportToAllure(String title, CrawlReport report) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total Inspected: %d%n", report.getTotalFound()));
        sb.append(String.format("Valid/Live: %d%n", report.getValidLinks().size()));
        sb.append(String.format("Broken: %d%n%n", report.getBrokenLinks().size()));

        if (!report.getBrokenLinks().isEmpty()) {
            sb.append("BROKEN ASSETS:%n");
            for (LinkCheckResult broken : report.getBrokenLinks()) {
                sb.append(String.format("  [%d - %s] %s%n", broken.getStatusCode(), broken.getStatusMessage(), broken.getUrl()));
            }
        }
        return sb.toString();
    }
}
