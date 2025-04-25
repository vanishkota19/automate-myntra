package com.automation.framework.moduledriver;

import com.automation.framework.base.BasePage;
import com.automation.framework.utils.ExcelDataProvider;
import com.automation.framework.utils.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Module driver for login functionality
 */
public class LoginModule extends BasePage {
    
    private ExcelDataProvider excelData;
    private final String PAGE_NAME = "LoginPage";
    
    /**
     * Constructor
     * @param driver WebDriver instance
     * @param excelData ExcelDataProvider instance
     */
    public LoginModule(WebDriver driver, ExcelDataProvider excelData) {
        super(driver);
        this.excelData = excelData;
    }
    
    /**
     * Navigate to login page
     * @return this instance for method chaining
     */
    public LoginModule navigateToLoginPage() {
        String url = excelData.getTestData("LoginTest", "LoginURL");
        driver.get(url);
        waitForPageLoad();
        
        // Safely log to ExtentReport
        safeLog(Status.INFO, "Navigated to login page: " + url);
        return this;
    }
    
    /**
     * Enter username
     * @param username username to enter
     * @return this instance for method chaining
     */
    public LoginModule enterUsername(String username) {
        String usernameXpath = excelData.getXPath(PAGE_NAME, "usernameField");
        WebElement usernameField = findElementByXpath(usernameXpath);
        sendKeys(usernameField, username);
        
        safeLog(Status.INFO, "Entered username: " + username);
        return this;
    }
    
    /**
     * Enter password
     * @param password password to enter
     * @return this instance for method chaining
     */
    public LoginModule enterPassword(String password) {
        String passwordXpath = excelData.getXPath(PAGE_NAME, "passwordField");
        WebElement passwordField = findElementByXpath(passwordXpath);
        sendKeys(passwordField, password);
        
        safeLog(Status.INFO, "Entered password");
        return this;
    }
    
    /**
     * Click login button
     * @return this instance for method chaining
     */
    public LoginModule clickLoginButton() {
        String loginButtonXpath = excelData.getXPath(PAGE_NAME, "loginButton");
        WebElement loginButton = findElementByXpath(loginButtonXpath);
        click(loginButton);
        
        safeLog(Status.INFO, "Clicked login button");
        return this;
    }
    
    /**
     * Verify login error message
     * @param expectedMessage expected error message
     * @return true if error message matches
     */
    public boolean verifyErrorMessage(String expectedMessage) {
        String errorMsgXpath = excelData.getXPath(PAGE_NAME, "errorMessage");
        WebElement errorElement = findElementByXpath(errorMsgXpath);
        String actualMessage = getText(errorElement);
        
        boolean matches = actualMessage.contains(expectedMessage);
        if (matches) {
            safeLog(Status.PASS, "Error message verified. Expected: " + expectedMessage + ", Actual: " + actualMessage);
        } else {
            safeLog(Status.FAIL, "Error message did not match. Expected: " + expectedMessage + ", Actual: " + actualMessage);
        }
        
        return matches;
    }
    
    /**
     * Complete login process
     * @param username username
     * @param password password
     * @return this instance for method chaining
     */
    public LoginModule login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
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