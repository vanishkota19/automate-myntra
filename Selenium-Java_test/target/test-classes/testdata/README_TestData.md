# Test Data Excel Structure

The `TestData.xlsx` file contains all test data needed for the automation tests. It needs to be created with the following structure:

## Sheet: TestData

The main sheet containing all test data values organized by test case ID and parameter.

| TestCaseID | LoginURL                       | ValidUsername | ValidPassword | InvalidUsername | InvalidPassword | ErrorMessage                 | EmptyCredentialsError       | DashboardTitle |
|------------|--------------------------------|---------------|---------------|-----------------|-----------------|------------------------------|------------------------------|----------------|
| LoginTest  | https://www.example.com/login  | validuser     | validpass123  | invaliduser     | invalidpass     | Invalid username or password | Please enter your credentials| Dashboard      |
| MyntraTest |                                |               |               |                 |                 |                              |                              |                |

## Myntra Test Data

| TestCaseID | ProductSearchTerm |
|------------|--------------------|
| MyntraTest | Men T-shirts      |

## Guidelines:

1. The first column should always be the TestCaseID
2. Each row represents a set of data for a specific test
3. Each column represents a specific data parameter
4. Maintain consistency in parameter naming across the framework 