package com.aib.listeners;

import com.aib.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Custom TestNG Listener capturing lifecycle events, success & failure screenshots, and summary banner.
 */
public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);
    private static final AtomicInteger passedCount = new AtomicInteger(0);
    private static final AtomicInteger failedCount = new AtomicInteger(0);
    private static final AtomicInteger skippedCount = new AtomicInteger(0);

    @Override
    public void onStart(ITestContext context) {
        log.info("==========================================================================");
        log.info("           STARTING TEST SUITE: {}                                        ", context.getName());
        log.info("==========================================================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info(">>> [STARTING] Test: {}() in class {}",
                result.getMethod().getMethodName(),
                result.getTestClass().getRealClass().getSimpleName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedCount.incrementAndGet();
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        log.info(">>> [PASS] Test: {}() finished successfully in {}ms", testName, duration);

        // Capture screenshot on SUCCESS and attach to Allure Report
        byte[] screenshotBytes = ScreenshotUtils.captureScreenshotForReport(testName, "PASSED");
        if (screenshotBytes != null && screenshotBytes.length > 0) {
            Allure.addAttachment(testName + "_SUCCESS_Screenshot", new ByteArrayInputStream(screenshotBytes));
        }
        ScreenshotUtils.saveScreenshotToFile(testName, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedCount.incrementAndGet();
        String testName = result.getMethod().getMethodName();
        log.error(">>> [FAIL] Test: {}() failed with error: {}", testName, result.getThrowable().getMessage());

        // Capture screenshot on FAILURE and attach to Allure Report
        byte[] screenshotBytes = ScreenshotUtils.captureScreenshotForReport(testName, "FAILED");
        if (screenshotBytes != null && screenshotBytes.length > 0) {
            Allure.addAttachment(testName + "_FAILURE_Screenshot", new ByteArrayInputStream(screenshotBytes));
        }
        ScreenshotUtils.saveScreenshotToFile(testName, "FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedCount.incrementAndGet();
        log.warn(">>> [SKIPPED] Test: {}() was skipped", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        int passed = passedCount.get();
        int failed = failedCount.get();
        int skipped = skippedCount.get();
        int total = passed + failed + skipped;

        System.out.println("\n");
        System.out.println("==========================================================================");
        System.out.println("                   AIB UI TEST EXECUTION SUMMARY                          ");
        System.out.println("==========================================================================");
        System.out.printf("  TOTAL TESTS RUN   : %d%n", total);
        System.out.printf("  SUCCESSFUL TESTS  : %d%n", passed);
        System.out.printf("  FAILED TESTS      : %d%n", failed);
        System.out.printf("  SKIPPED TESTS     : %d%n", skipped);
        System.out.println("--------------------------------------------------------------------------");

        if (failed == 0 && total > 0) {
            System.out.println("  🎉 SUCCESS: ALL " + total + " TEST CASES COMPLETED AND PASSED PERFECTLY!");
            System.out.println("  Screenshots captured for all successful tests in: target/screenshots/passed/");
        } else {
            System.out.println("  ⚠️ COMPLETED WITH " + failed + " FAILURES. Check target/screenshots/failed/");
        }
        System.out.println("==========================================================================\n");
    }
}
