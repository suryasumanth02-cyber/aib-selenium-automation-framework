# AIB (Allied Irish Banks) Enterprise UI Test Automation Framework

[![Java CI](https://img.shields.io/badge/Language-Java%2017%2F21%2F25-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.29.0-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red.svg)](https://testng.org/)
[![Axe Core](https://img.shields.io/badge/Accessibility-WCAG%202.1%20AA%20(axe--core)-blueviolet.svg)](https://www.deque.com/axe/)
[![CDP](https://img.shields.io/badge/Chrome%20DevTools%20Protocol-CDP%20Emulation-blue.svg)](https://chromedevtools.github.io/devtools-protocol/)
[![Docker](https://img.shields.io/badge/Selenium%20Grid-Docker%20Compose-2496ED.svg)](https://www.docker.com/)
[![Allure](https://img.shields.io/badge/Reporting-Allure%202.29.0-yellow.svg)](https://docs.qameta.io/allure/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions%20%26%20Pages-2088FF.svg)](https://github.com/)

An enterprise-grade UI test automation framework built from scratch targeting public customer-facing financial tools on the **AIB (Allied Irish Banks - `aib.ie`)** web platform. Designed to demonstrate production-level SDET best practices for top Irish financial institutions, multinational tech firms, and fintech companies (AIB, Bank of Ireland, Stripe, Fidelity Investments, Citi, Workday).

---

## 🏗️ Architecture & Project Structure

The project strictly follows the clean, modular VS Code package layout:

```
SeleniumUiTesting/
├── .github/workflows/
│   └── test-execution.yml        # GitHub Actions CI/CD deploying Allure to GitHub Pages
├── src/
│   ├── main/java/
│   │   ├── pages/                # Fluent Page Object Model (POM) & Component Objects
│   │   │   ├── BasePage.java
│   │   │   ├── AibHomePage.java
│   │   │   ├── MortgagePage.java
│   │   │   ├── LoansPage.java
│   │   │   ├── SavingsPage.java
│   │   │   └── CookieBannerComponent.java
│   │   └── support/              # Core Utilities & Automation Engines
│   │       ├── ConfigReader.java
│   │       ├── Constants.java
│   │       ├── DriverFactory.java
│   │       ├── DriverManager.java
│   │       ├── WaitUtils.java
│   │       ├── JavaScriptUtils.java
│   │       ├── ScreenshotUtils.java
│   │       ├── AccessibilityUtils.java  # Deque axe-core WCAG 2.1 AA auditor
│   │       ├── CdpUtils.java            # Selenium 4 CDP (Geolocation & Throttling)
│   │       └── NetworkHealthUtils.java  # Java 11 HttpClient hybrid crawler
│   └── test/java/
│       ├── support/              # Test lifecycle & Listeners
│       │   ├── BaseTest.java            # Single-session @BeforeClass / @AfterClass
│       │   └── TestListener.java        # Auto PASS/FAIL screenshots & execution banner
│       ├── tests/aib/            # Enterprise Regression & Functional Suites
│       │   ├── AibHomeTest.java         # 10 tests (UI, RGBA brand colors, navigation)
│       │   ├── MortgageTest.java        # 12 tests (Sliders, rates, calculations, RGBA)
│       │   ├── LoansTest.java           # 13 tests (Loan calculator, repayment, RGBA)
│       │   ├── SavingsTest.java         # 13 tests (Savings goal calculator, terms, RGBA)
│       │   ├── AccessibilityTest.java   # 3 tests (WCAG 2.1 AA scans on Home, Mortgages, Loans)
│       │   ├── CdpFeaturesTest.java     # 4 tests (Dublin/Cork Geolocation, Slow 3G, Console logs)
│       │   └── NetworkHealthTest.java   # 3 tests (Hyperlinks, image assets, regulatory links)
│       └── tests/smoke/
│           └── SmokeTest.java           # 3 fast smoke tests for CI sanity checks
├── src/test/resources/
│   ├── config.properties         # Global configuration (browsers, URLs, grid)
│   └── testng.xml                # Multi-threaded parallel execution suite
├── docker-compose.yml            # Selenium Grid 4 Hub + Chrome & Firefox nodes
└── pom.xml                       # Maven build with Surefire & Allure plugins
```

---

## 🌟 Advanced SDET Portfolio Features

### 1. ♿ Automated WCAG 2.1 Level AA Accessibility Auditing (axe-core)
- Integrated **Deque `axe-core`** to enforce **EU Directive 2019/882 (European Accessibility Act 2025)** compliance.
- Runs comprehensive full-page and component-level audits across AIB Homepage, Mortgage Calculator, and Personal Loans portals.
- Categorizes accessibility violations into **Critical, Serious, Moderate, and Minor**, generating structured reports attached directly to Allure test cases.
- Asserts zero blocking critical defects (color contrast, ARIA landmarks, form labels, keyboard reachability).

### 2. ⚡ Selenium 4 Chrome DevTools Protocol (CDP) Automation
- Leverages version-resilient `((ChromiumDriver) driver).executeCdpCommand()` bypassing CDP binding mismatches.
- **Irish Geolocation Emulation:**
  - **Dublin:** Latitude `53.349805`, Longitude `-6.260310`
  - **Cork:** Latitude `51.898514`, Longitude `-8.475603`
  - Emulates client position via `Emulation.setGeolocationOverride` and grants origin permissions via `Browser.grantPermissions` to test regional branch finders.
- **Network Throttling & Resilience:**
  - Simulates **Slow 3G** (400ms RTT, 500kbps throughput) via `Network.emulateNetworkConditions` to verify page stability under poor Irish transit/rural connectivity.
- **Console Log Auditing:**
  - Captures browser runtime errors and audits client-side JavaScript crashes via Selenium LogEntries / CDP Log domain.

### 3. 🌐 API & UI Hybrid Broken Link & Asset Crawler
- High-speed crawler combining Selenium DOM extraction with non-blocking **Java 11 `HttpClient`**.
- Concurrently audits hundreds of hyperlink `<a>` hrefs and image `<img>` sources using parallel `CompletableFuture` pools.
- Verifies image renderability by checking both HTTP response status and DOM `naturalWidth > 0` via JavaScript.
- Validates Central Bank of Ireland consumer disclosures, regulatory links, and privacy notices.

### 4. 🐳 Docker & Distributed Selenium Grid Execution
- Includes `docker-compose.yml` orchestrating:
  - `selenium-hub` (port `4444`)
  - `chrome-node` (shared memory `2gb`, 2 parallel browser sessions)
  - `firefox-node` (shared memory `2gb`, 2 parallel browser sessions)
- `DriverFactory` seamlessly switches between local drivers and `RemoteWebDriver` via `-Dremote=true` or `-Dgrid.url=http://localhost:4444/wd/hub`.

### 5. 🚀 CI/CD Pipeline & Live Allure Report Deployment
- Fully automated **GitHub Actions** workflow (`.github/workflows/test-execution.yml`).
- Runs headless tests on Ubuntu runners with Google Chrome Stable.
- Generates rich Allure HTML dashboards and deploys them directly to **GitHub Pages (`gh-pages`)** with historical trend tracking.
- Publishes job summaries and uploads screenshots of all test runs as build artifacts.

### 6. 🎨 RGBA Brand Color & Design System Verification
- Verifies AIB's digital design system colors (Primary Purple `rgba(128, 39, 137, 1)` / `#802789`, Active Coral `rgba(235, 114, 0, 1)`, Pure White `rgba(255, 255, 255, 1)`).
- Ensures CSS styling, buttons, active tabs, and promotional badges adhere to exact brand hex/RGBA specifications.

### 7. 📸 Smart Screenshot Capture & Execution Banner
- Single browser session per test class via `@BeforeClass` and `@AfterClass` to minimize browser overhead.
- Automatic full-screen screenshot capture for:
  - **Successful Tests:** saved to `target/screenshots/passed/`
  - **Failed Tests:** saved to `target/screenshots/failed/`
- Clean ASCII execution summary banner outputting total tests, passed, failed, and execution status.

---

## 📊 Complete Test Suite Inventory (61 Tests)

| Class | Tests | TestNG Groups | Key Validations |
| :--- | :---: | :--- | :--- |
| **`AibHomeTest`** | **10** | `home`, `ui`, `rgba`, `regression` | Page title, logo visibility, primary navigation menu, Daily Banking CTA, RGBA purple brand banner, search trigger, mobile app promo, security badge, ways to bank, and footer legal disclaimer. |
| **`MortgageTest`** | **12** | `mortgage`, `calculator`, `rgba`, `regression` | Calculator header, First Time Buyer tab, RGBA active tab style, property value & deposit sliders, calculate button, monthly repayment estimate, borrowing capacity, reset function, disclaimer, and rates. |
| **`LoansTest`** | **13** | `loans`, `calculator`, `rgba`, `regression` | Loans landing page, calculator inputs, term dropdown, monthly repayment result, APR interest rate, apply CTA button, Car loan option, Home Improvement option, personal loan rates, and RGBA CTA button. |
| **`SavingsTest`** | **13** | `savings`, `deposits`, `rgba`, `regression` | Savings landing page, Regular Saver account, Deposit accounts, interest rate display, online opening CTA, calculator goal input, term options, monthly contribution estimate, RGBA buttons, and help link. |
| **`AccessibilityTest`**| **3** | `accessibility`, `compliance`, `regression` | WCAG 2.1 AA audit on AIB Homepage, Mortgage Calculator, and Loans page with axe-core; asserts zero critical blocking defects. |
| **`CdpFeaturesTest`** | **4** | `cdp`, `geolocation`, `network`, `console` | Emulates Dublin coordinates (53.3498, -6.2603), Cork coordinates (51.8985, -8.4756), Slow 3G throttling (400ms latency), and browser console JS error audit. |
| **`NetworkHealthTest`**| **3** | `network`, `crawler`, `compliance`, `regression`| 20+ homepage hyperlinks status check, 20+ brand image naturalWidth validation, and Central Bank / regulatory links reachability. |
| **`SmokeTest`** | **3** | `smoke`, `sanity` | Fast pre-merge smoke verification of Homepage, Mortgages, and Loans portals. |
| **TOTAL** | **61** | — | **100% Passing in ~2 minutes with parallel class execution** |

---

## 💻 How to Run the Tests

### Prerequisites
- **Java JDK:** 17, 21, or 25
- **Apache Maven:** 3.9+
- **Google Chrome:** (or Firefox / Edge)

### 1. Run the Entire Regression Suite (Default: Headless Chrome)
```bash
mvn clean test
```

### 2. Run Targeted Test Suites by Group
```bash
# Run only WCAG 2.1 AA Accessibility audits
mvn test -Dgroups=accessibility

# Run only Chrome DevTools Protocol (CDP) tests
mvn test -Dgroups=cdp

# Run only the Broken Link & Asset Crawler
mvn test -Dgroups=crawler

# Run only Mortgage Calculator tests
mvn test -Dgroups=mortgage

# Run only RGBA color design tests
mvn test -Dgroups=rgba

# Run fast Smoke sanity tests
mvn test -Dgroups=smoke
```

### 3. Run with Visible Browser UI (Headed Mode)
```bash
mvn test -Dheadless=false
```

### 4. Cross-Browser Execution
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### 5. Distributed Execution via Docker & Selenium Grid
```bash
# Start Selenium Grid Hub, Chrome & Firefox containers
docker compose up -d

# Verify Grid status at http://localhost:4444
# Run tests against the distributed Grid
mvn test -Dremote=true

# Stop Grid
docker compose down
```

### 6. Generate & Open Allure Report
```bash
# Generate HTML report
mvn allure:report

# Serve and open interactive dashboard in browser
mvn allure:serve
```

---

## 🎯 How to Pitch This Project in Ireland Tech Interviews

Use this executive summary on your resume and in SDET interviews:

```
Role: Senior SDET / QA Automation Engineer
Project: Enterprise AIB Digital Banking UI Automation Framework
Tech Stack: Java 17/25, Selenium WebDriver 4.29, TestNG 7.10, Deque axe-core, Chrome DevTools Protocol (CDP), Java 11 HttpClient, Docker, Allure 2.29, GitHub Actions

Key Accomplishments:
• Architected a thread-safe UI test automation framework from scratch for Allied Irish Banks (aib.ie), scaling to 61 automated test cases with 100% green execution.
• Built automated WCAG 2.1 Level AA accessibility auditing using Deque axe-core, validating compliance with the European Accessibility Act 2025 across banking portals.
• Implemented Selenium 4 Chrome DevTools Protocol (CDP) for Dublin/Cork geolocation emulation and Slow 3G network throttling to simulate rural Irish connectivity.
• Designed a hybrid API & UI crawler utilizing Java 11 HttpClient and CompletableFuture parallel streams to validate hundreds of hyperlinks and image assets in seconds.
• Automated RGBA design system color validations, verifying brand primary purple (#802789) and interactive button states.
• Configured distributed test execution with Docker Compose and Selenium Grid 4, enabling scalable containerized cross-browser runs.
• Built a GitHub Actions CI/CD pipeline deploying live Allure reporting dashboards to GitHub Pages with automated failure screenshot capture.
```
