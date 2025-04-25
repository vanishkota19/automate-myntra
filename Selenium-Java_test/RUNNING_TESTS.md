# Running the Tests

To run the tests in this framework, follow these steps:

## Prerequisites

1. Make sure you have Java JDK 11 or higher installed
2. Make sure Maven is installed and configured

## Creating Excel Data Files

Before running the tests, you need to create two Excel files:

1. `src/test/resources/testdata/TestData.xlsx`
2. `src/test/resources/testdata/XPathRepo.xlsx`

See `src/test/resources/testdata/README_IMPORTANT.txt` for the exact structure of these files.

## Running Tests Using Maven

```bash
# Run all tests with default Chrome browser
mvn clean test

# Run with a specific TestNG XML configuration
mvn clean test -DsuiteXmlFile=testng.xml
```

## Running Tests Using Eclipse/IntelliJ

1. Right-click on the `testng.xml` file
2. Select "Run As" > "TestNG Suite"

Or:

1. Right-click on a specific test class like `LoginTest.java`
2. Select "Run As" > "TestNG Test"

## Troubleshooting

### Parameter 'browser' Error

If you get an error like:
```
Parameter 'browser' is required by BeforeMethod on method setUpTest but has not been marked @Optional or defined
```

Make sure you're running the tests through the TestNG XML file (`testng.xml`), which provides the browser parameter.

### Excel File Not Found

If you get warnings about Excel files not found, make sure you've created:
1. `src/test/resources/testdata/TestData.xlsx`
2. `src/test/resources/testdata/XPathRepo.xlsx`

The framework will use default values for missing data, but it's better to have proper Excel files set up. 