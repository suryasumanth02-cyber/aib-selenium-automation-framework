# AIB (Allied Irish Banks) Digital Banking UI Automation Framework

[![Java CI](https://img.shields.io/badge/Language-Java%2017%2F21%2B-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.29.0-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red.svg)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Reporting-Allure-yellow.svg)](https://docs.qameta.io/allure/)
[![Build Status](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blue.svg)](https://github.com/)

An enterprise-grade UI test automation framework built from scratch targeting public customer-facing financial tools on the **AIB (Allied Irish Banks - `aib.ie`)** web platform.

---

## Architecture Overview

```
SeleniumUiTesting/
├── .github/workflows/
│   └── test-execution.yml        # CI/CD GitHub Actions pipeline
├── src/
│   ├── main/java/com/aib/
│   │   ├── config/               # ConfigReader supporting System property overrides
│   │   ├── constants/            # FrameworkConstants (timeouts, paths)
│   │   ├── driver/               # ThreadLocal DriverManager & DriverFactory
│   │   ├── enums/                # BrowserType (Chrome, Firefox, Edge)
│   │   ├── pages/                # Page Objects (Fluent POM) & CookieBannerComponent
│   │   └── utils/                # WaitUtils, JavaScriptUtils, ScreenshotUtils
│   └── test/java/com/aib/
│       ├── base/                 # BaseTest setup/teardown lifecycle
│       ├── listeners/            # TestListener (auto-screenshot on failure for Allure)
│       └── tests/                # Parallel E2E Test suites
├── src/test/resources/
│   ├── config.properties         # Global configuration settings
│   └── testng.xml                # Parallel suite execution configuration
└── pom.xml                       # Maven build configuration
```

---

## Key Technical Features

### 1. Concurrency & Parallel Execution (`ThreadLocal<WebDriver>`)
- Built using `ThreadLocal<WebDriver>` within `DriverManager` to maintain isolated browser sessions per thread.
- Configured in `testng.xml` for multi-threaded parallel execution across test classes (`thread-count="2"`).

### 2. Design Patterns Implemented
- **Page Object Model (POM):** Strict encapsulation of DOM locators (`By`) from test logic.
- **Fluent / Method Chaining:** Action methods return page instances for readable, self-documenting tests.
- **Factory Pattern (`DriverFactory`):** Centralized browser instantiation supporting Chrome, Firefox, and Edge.
- **Singleton Pattern (`ConfigReader`):** Thread-safe configuration loading with runtime override support.

### 3. Anti-Bot / Stealth Automation
- Configured ChromeOptions with `--disable-blink-features=AutomationControlled`, sanitized User-Agents, and excluded automation switches to prevent Akamai / Cloudflare false positives during test runs.

### 4. Dynamic GDPR Cookie Consent Handling (`OneTrust`)
- Reusable `CookieBannerComponent` integrated into `BaseTest` to automatically detect, accept, and dismiss European GDPR cookie modals without breaking test timing.

### 5. Robust Synchronization (Zero `Thread.sleep()`)
- Dynamic explicit and fluent waiting via `WaitUtils` (`ExpectedConditions.visibilityOfElementLocated`, `elementToBeClickable`).

---

## Test Suites Covered

| Test Suite | Target Module | Scenarios Automated |
| :--- | :--- | :--- |
| **`MortgageCalculatorTest`** | `https://mymortgage.aib.ie/mortgages/calculator` | • Introductory CTA validation<br>• Data-Driven test for **Single vs Joint applicants** using `@DataProvider`<br>• Multi-step toolbar navigation persistence |
| **`BranchLocatorTest`** | `https://branches.aib.ie/search` | • Branch search input availability<br>• Data-driven geographic search for **Dublin** and **Cork**<br>• Verification of dynamic location result cards |
| **`FxRatesTest`** | `https://www.aib.ie/fxcentre/tools/fxrates-calculator` | • FX currency conversion inputs validation<br>• Default base currency assertion (EUR) |
| **`AibNavigationTest`** | `https://www.aib.ie/` | • Header and brand identity verification<br>• Mega-menu navigation into Mortgages portal |

---

## Running Tests Locally

### Prerequisites
- Java JDK 17+ (or Java 21 / 25)
- Apache Maven 3.9+
- Google Chrome browser installed

### 1. Run all tests in Headless mode (Default)
```bash
mvn clean test
```

### 2. Run tests with visible browser UI
```bash
mvn clean test -Dheadless=false
```

### 3. Cross-Browser Execution
```bash
# Run on Firefox
mvn clean test -Dbrowser=firefox

# Run on Microsoft Edge
mvn clean test -Dbrowser=edge
```

### 4. Generate & View Allure Test Report
```bash
mvn allure:serve
```

---

## CI/CD Pipeline (GitHub Actions)

A complete workflow (`.github/workflows/test-execution.yml`) runs headless tests on push and pull requests, builds the project, generates Allure reports, and uploads artifacts for test failure analysis.

---

## Resume & Interview Summary

```
Project: AIB (Allied Irish Banks) UI Test Automation Framework
Role: SDET / Automation QA Engineer
Stack: Java 17, Selenium WebDriver 4, TestNG, Maven, Allure Reports, GitHub Actions CI/CD

Key Accomplishments:
• Designed a thread-safe parallel test framework using Java 17 and Selenium 4 for AIB's digital banking tools.
• Implemented ThreadLocal WebDriver management enabling parallel test execution, cutting suite runtimes in half.
• Automated multi-step financial workflows including the AIB Mortgage Calculator and FX Rates tools using TestNG DataProviders.
• Built a resilient GDPR/OneTrust consent component handling dynamic overlays across European web portals.
• Integrated Allure Reporting with custom TestNG listeners to automatically capture DOM failure screenshots.
• Configured a GitHub Actions CI pipeline running automated regression suites on every pull request.
```
