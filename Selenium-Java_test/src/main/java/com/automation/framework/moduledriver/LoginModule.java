package com.automation.framework.moduledriver;

import com.automation.framework.base.BasePage;
import com.automation.framework.utils.ExcelDataProvider;
import com.automation.framework.utils.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 * Module driver class for Login functionality
 */
public class LoginModule extends BasePage {
    
    private ExcelDataProvider excelData;
    private final String PAGE_NAME = "LoginPage";
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public LoginModule(WebDriver driver) {
        super(driver);
        this.excelData = new ExcelDataProvider();
    }
    
    /**
     * Navigate to login page
     */
    public void navigateToLoginPage() {
        try {
            String loginUrl = excelData.getTestData("LoginTest", "LoginURL");
            if (loginUrl != null && !loginUrl.isEmpty()) {
                driver.get(loginUrl);
                waitForPageLoad();
            } else {
                // Default to Myntra login page if URL not found
                driver.get("https://www.myntra.com/login");
                waitForPageLoad();
            }
        } catch (Exception e) {
            System.out.println("Error navigating to login page: " + e.getMessage());
            // Default to Myntra login page on error
            driver.get("https://www.myntra.com/login");
            waitForPageLoad();
        }
    }
    
    /**
     * Enter username
     */
    public void enterUsername(String username) {
        try {
            String xpath = excelData.getXPath("LoginPage", "usernameField");
            WebElement usernameField = findElementByXpath(xpath);
            if (usernameField != null) {
                sendKeys(usernameField, username);
            } else {
                System.out.println("Username field not found");
            }
        } catch (Exception e) {
            System.out.println("Error entering username: " + e.getMessage());
        }
    }
    
    /**
     * Enter password
     */
    public void enterPassword(String password) {
        try {
            String xpath = excelData.getXPath("LoginPage", "passwordField");
            WebElement passwordField = findElementByXpath(xpath);
            if (passwordField != null) {
                sendKeys(passwordField, password);
            } else {
                System.out.println("Password field not found");
            }
        } catch (Exception e) {
            System.out.println("Error entering password: " + e.getMessage());
        }
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        try {
            String xpath = excelData.getXPath("LoginPage", "loginButton");
            WebElement loginButton = findElementByXpath(xpath);
            if (loginButton != null) {
                click(loginButton);
                waitForPageLoad();
            } else {
                System.out.println("Login button not found");
            }
        } catch (Exception e) {
            System.out.println("Error clicking login button: " + e.getMessage());
        }
    }
    
    /**
     * Verify error message
     */
    public void verifyErrorMessage(String expectedMessage) {
        try {
            String xpath = excelData.getXPath("LoginPage", "errorMessage");
            WebElement errorElement = findElementByXpath(xpath);
            if (errorElement != null) {
                String actualMessage = errorElement.getText();
                Assert.assertEquals(actualMessage, expectedMessage, "Error message mismatch");
            } else {
                System.out.println("Error message element not found");
            }
        } catch (Exception e) {
            System.out.println("Error verifying error message: " + e.getMessage());
        }
    }
    
    /**
     * Verify successful login
     */
    public void verifySuccessfulLogin() {
        try {
            String expectedTitle = excelData.getTestData("LoginTest", "DashboardTitle");
            if (expectedTitle != null && !expectedTitle.isEmpty()) {
                String actualTitle = driver.getTitle();
                Assert.assertEquals(actualTitle, expectedTitle, "Login unsuccessful - Title mismatch");
            } else {
                // Default check for Myntra dashboard
                String actualTitle = driver.getTitle();
                Assert.assertTrue(actualTitle.contains("Myntra"), "Login unsuccessful - Not on Myntra dashboard");
            }
        } catch (Exception e) {
            System.out.println("Error verifying successful login: " + e.getMessage());
        }
    }
    
    /**
     * Safely log to ExtentReport, handling null ExtentTest
     * @param status the log status
     * @param message the message to log
     */
    private void safeLog(Status status, String message) {
        ExtentTest test = ExtentReportManager.getInstance().getTest();
        if (test != null) {
            test.log(status, message);
        } else {
            // Fall back to console logging if ExtentTest is not available
            System.out.println(status + ": " + message);
        }
    }
} 