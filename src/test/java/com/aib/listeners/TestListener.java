package com.aib.listeners;

import com.aib.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

/**
 * Custom TestNG Listener capturing lifecycle events and test failure screenshots for Allure.
 */
public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("====== Starting Test Suite: {} ======", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("====== Completed Test Suite: {} ======", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info(">>> Running Test: {}() in class {}", result.getMethod().getMethodName(), result.getTestClass().getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info(">>> [PASS] Test: {}() succeeded in {}ms",
                result.getMethod().getMethodName(),
                (result.getEndMillis() - result.getStartMillis()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error(">>> [FAIL] Test: {}() failed with error: {}", testName, result.getThrowable().getMessage());

        // Capture screenshot and attach to Allure Report
        byte[] screenshotBytes = ScreenshotUtils.captureScreenshotForReport(testName);
        if (screenshotBytes != null && screenshotBytes.length > 0) {
            Allure.addAttachment(testName + "_Failure_Screenshot", new ByteArrayInputStream(screenshotBytes));
        }

        ScreenshotUtils.saveScreenshotToFile(testName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn(">>> [SKIPPED] Test: {}() was skipped", result.getMethod().getMethodName());
    }
}
