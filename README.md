# AIB Web UI Automation Framework

This is an end-to-end UI test automation framework I built from scratch for the Allied Irish Banks (AIB - aib.ie) public web platform. The project is implemented in Java 17+ with Selenium WebDriver 4, TestNG, and Maven, and includes Allure reporting and a GitHub Actions CI pipeline.

I built this project to simulate real-world financial web automation. It covers 61 automated tests across customer journeys like mortgage calculation, personal loan estimation, savings goals, brand visual checks, accessibility compliance, and network/location emulation.

---

## Why I Built This

I wanted to build a testing framework that goes beyond basic button clicking and actually addresses the real challenges of automated testing on enterprise web applications:

1. Dynamic overlays and third-party consent dialogs (such as OneTrust GDPR banners).
2. Bot detection and automation flags (Akamai).
3. Accessibility regulations (WCAG 2.1 AA / European Accessibility Act 2025).
4. Network and location behavior (CDP emulation for geolocation and throttling).
5. Fast feedback loops (reusing browser instances per test class, parallel test runs, and headless CI).

---

## Tech Stack

- Language: Java 17+ (tested on Java 17 and 25)
- Automation Tool: Selenium WebDriver 4.29.0
- Test Runner: TestNG 7.10.2
- Build Tool: Apache Maven
- Accessibility Engine: Deque axe-core 4.10.1
- Network / HTTP: Java 11 java.net.http.HttpClient
- Reporting: Allure 2.29.0 & TestNG XML reports
- Containerization: Docker & Docker Compose (Selenium Grid 4)
- CI/CD: GitHub Actions with GitHub Pages report deployment

---

## Project Structure

I structured the project using a standard Maven layout with clean separation between page objects, framework support utilities, and test suites:

```
SeleniumUiTesting/
├── .github/
│   └── workflows/
│       └── test-execution.yml     # GitHub Actions pipeline
├── src/
│   ├── main/java/
│   │   ├── pages/                 # Page Object Model classes
│   │   │   ├── BasePage.java
│   │   │   ├── AibHomePage.java
│   │   │   ├── MortgagePage.java
│   │   │   ├── LoansPage.java
│   │   │   ├── SavingsPage.java
│   │   │   └── CookieBannerComponent.java
│   │   └── support/               # Framework utilities
│   │       ├── ConfigReader.java
│   │       ├── Constants.java
│   │       ├── DriverFactory.java
│   │       ├── DriverManager.java
│   │       ├── WaitUtils.java
│   │       ├── JavaScriptUtils.java
│   │       ├── ScreenshotUtils.java
│   │       ├── AccessibilityUtils.java
│   │       ├── CdpUtils.java
│   │       └── NetworkHealthUtils.java
│   └── test/java/
│       ├── support/               # Test lifecycle & listener
│       │   ├── BaseTest.java
│       │   └── TestListener.java
│       ├── tests/aib/             # Functional and technical test classes
│       │   ├── AibHomeTest.java
│       │   ├── MortgageTest.java
│       │   ├── LoansTest.java
│       │   ├── SavingsTest.java
│       │   ├── AccessibilityTest.java
│       │   ├── CdpFeaturesTest.java
│       │   └── NetworkHealthTest.java
│       └── tests/smoke/
│           └── SmokeTest.java
├── src/test/resources/
│   ├── config.properties          # Global test configuration
│   └── testng.xml                 # Suite file configured for parallel execution
├── docker-compose.yml             # Selenium Grid Hub + Chrome and Firefox nodes
└── pom.xml                        # Maven dependencies and build configuration
```

---

## Core Framework Design Decisions

### 1. Page Object Model (POM) with Fluent Interfaces
I separated test logic from DOM locators by modeling each webpage (and self-contained components like the cookie banner) as its own class. Methods return page instances where applicable to allow readable method chaining in tests.

### 2. Thread-Safe WebDriver Management
I wrapped the WebDriver instance inside a ThreadLocal within `DriverManager`. This ensures tests running in parallel across multiple classes never share or corrupt each other's browser state.

### 3. Single Browser Session per Test Class
Instead of spinning up and quitting a browser for every individual test method (which causes massive overhead), I manage driver initialization inside `@BeforeClass` and teardown in `@AfterClass`. This dropped the total execution time for all 61 tests down to around 2 minutes.

### 4. Zero Thread.sleep()
All waiting logic relies on dynamic explicit waits in `WaitUtils` (such as `visibilityOfElementLocated` and `elementToBeClickable`).

### 5. Automatic Screenshot on Pass & Fail
Through a custom TestNG listener (`TestListener`), every passed test automatically captures a timestamped screenshot saved under `target/screenshots/passed/`. Any failure captures a screenshot under `target/screenshots/failed/` and attaches it directly to the Allure report.

### 6. WCAG 2.1 AA Accessibility Testing (axe-core)
I integrated Deque's `axe-core` library to run automated accessibility audits against the live AIB pages. The audit runs against `wcag2a`, `wcag2aa`, `wcag21a`, and `wcag21aa` rules, categorizing findings into Critical, Serious, Moderate, and Minor violations.

### 7. Selenium 4 Chrome DevTools Protocol (CDP)
I used Chromium's CDP raw commands (`executeCdpCommand`) instead of version-locked devtools packages. This allowed me to:
- Emulate geographical coordinates for Dublin (53.3498, -6.2603) and Cork (51.8985, -8.4756) to test branch location behavior.
- Emulate mobile transit network conditions (latency and throughput throttling).
- Capture and inspect browser console logs for severe JavaScript errors.

### 8. Hybrid API & UI Crawler
Instead of navigating sequentially to every link through the browser (which takes minutes), I built `NetworkHealthUtils` using Java 11 `HttpClient` and `CompletableFuture`. It pulls all link and image targets from the page using Selenium and validates their HTTP status codes concurrently in a few seconds.

### 9. Docker & Selenium Grid
I created a `docker-compose.yml` file to spin up a Selenium Hub with Chrome and Firefox nodes. `DriverFactory` detects when `-Dremote=true` is set and connects to the Grid via `RemoteWebDriver`.

---

## Test Coverage (61 Tests Total)

| Test Class | Scenarios | Groups | What Is Tested |
| :--- | :---: | :--- | :--- |
| AibHomeTest | 10 | home, ui, rgba, regression | Header, branding, navigation menu, Daily Banking CTA, RGBA purple brand color, search bar, app promo, security badge, ways to bank, and legal disclaimers. |
| MortgageTest | 12 | mortgage, calculator, rgba, regression | Mortgage calculator header, First Time Buyer tab, active tab styles, property value and deposit inputs, calculate button, monthly estimates, borrowing capacity, reset function, disclaimer, and interest rates. |
| LoansTest | 13 | loans, calculator, rgba, regression | Loan calculator fields, repayment amounts, term dropdowns, APR rates, Apply CTA, Car loan option, Home improvement option, personal rates, and button RGBA colors. |
| SavingsTest | 13 | savings, deposits, rgba, regression | Savings landing page, Regular Saver, Fixed Term deposits, interest rate displays, online opening links, savings goal calculator, term options, and button styling. |
| AccessibilityTest | 3 | accessibility, compliance, regression | Full WCAG 2.1 AA accessibility scans across Homepage, Mortgage Calculator, and Loans pages. |
| CdpFeaturesTest | 4 | cdp, geolocation, network, console | Dublin geolocation override, Cork geolocation override, network throttling, and browser console error auditing. |
| NetworkHealthTest | 3 | network, crawler, compliance, regression | Concurrent hyperlink validation, image render checks (naturalWidth > 0), and regulatory disclosure links. |
| SmokeTest | 3 | smoke, sanity | High-level sanity checks ensuring Homepage, Mortgages, and Loans portals are reachable. |

---

## How to Run the Tests

### Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.9+
- Google Chrome (or Firefox / Edge)

### Run all tests (headless Chrome by default)
```bash
mvn clean test
```

### Run with browser visible (headed mode)
```bash
mvn test -Dheadless=false
```

### Run specific test groups
```bash
# Run only accessibility audits
mvn test -Dgroups=accessibility

# Run only CDP DevTools tests
mvn test -Dgroups=cdp

# Run only broken link and image crawler
mvn test -Dgroups=crawler

# Run only mortgage calculator tests
mvn test -Dgroups=mortgage

# Run only brand color RGBA tests
mvn test -Dgroups=rgba

# Run smoke tests
mvn test -Dgroups=smoke
```

### Cross-browser testing
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Run with Docker Selenium Grid
```bash
# Start the grid
docker compose up -d

# Run tests on the remote grid
mvn test -Dremote=true

# Stop the grid
docker compose down
```

### Generate and view the Allure report
```bash
mvn allure:report
mvn allure:serve
```

---

## CI/CD Pipeline

The repository includes a GitHub Actions workflow in `.github/workflows/test-execution.yml` that:
1. Checks out the code and sets up JDK 17 on an Ubuntu runner.
2. Runs the full test suite in headless Chrome.
3. Generates the Allure HTML report.
4. Uploads test screenshots and surefire reports as job artifacts.
5. Deploys the Allure report to GitHub Pages on every push to main.
