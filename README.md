# Selenium Java Automation Framework

A modular automation framework built with Selenium WebDriver, Java, TestNG, Maven, and ExtentReports. The framework uses Excel files for test data and XPath storage to promote maintainability and separation of concerns.

## Features

- **Page Object Model (POM)** design pattern
- **Module Driver Pattern** - reusable actions grouped into functional modules
- **Data-Driven Testing** with Excel files for test data
- **External XPath Repository** in Excel format
- **ExtentReports** for beautiful HTML test reports
- **TestNG** for test orchestration and parallel execution
- **Maven** for dependency management and build
- **Cross-browser support** for Chrome, Firefox, and Edge
- **TestNG Listeners** for test event handling and reporting

## Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── automation
│   │   │           └── framework
│   │   │               ├── base
│   │   │               │   ├── BasePage.java
│   │   │               │   └── BaseTest.java
│   │   │               ├── config
│   │   │               ├── listeners
│   │   │               │   └── TestListener.java
│   │   │               ├── moduledriver
│   │   │               │   └── LoginModule.java
│   │   │               └── utils
│   │   │                   ├── ExcelDataProvider.java
│   │   │                   └── ExtentReportManager.java
│   │   └── resources
│   │       └── config.properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── automation
│       │           └── tests
│       │               └── LoginTest.java
│       └── resources
│           └── testdata
│               ├── README_TestData.md
│               ├── README_XPathRepo.md
│               ├── TestData.xlsx
│               └── XPathRepo.xlsx
├── pom.xml
├── testng.xml
└── README.md
```

## Setup and Configuration

### Prerequisites

- Java JDK 11 or higher
- Maven
- Browsers: Chrome, Firefox, and/or Edge

### Dependencies

The framework uses the following key dependencies:
- Selenium WebDriver
- TestNG
- ExtentReports
- Apache POI (for Excel operations)
- WebDriverManager

### Configuration

1. Edit the `src/main/resources/config.properties` file to set:
   - Application URL
   - Browser settings
   - Other configuration parameters

2. Create Excel files:
   - TestData.xlsx: Contains all test data
   - XPathRepo.xlsx: Contains all XPath expressions

## Running Tests

### Using Maven

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn clean test -Dtest=LoginTest

# Run with specific TestNG suite
mvn clean test -DsuiteXmlFile=testng.xml
```

### Using TestNG directly

Run the `testng.xml` file directly from your IDE.

## Extending the Framework

### Adding a New Page/Module

1. Create a new class in the `moduledriver` package that extends `BasePage`
2. Add the page name to the XPathRepo.xlsx file as a new sheet
3. Implement the methods required for the module's functionality

### Adding a New Test

1. Create a new test class that extends `BaseTest`
2. Add test data to TestData.xlsx
3. Create test methods with TestNG annotations

## Best Practices

1. Keep XPath expressions in the Excel file, not hardcoded in the Java code
2. Use descriptive names for test methods and module methods
3. Each test should focus on a specific functionality
4. Use the module driver pattern to create reusable actions
5. Handle waits appropriately in the BasePage methods

## Authors

-Vanish Kota

