package com.automation.tests;

import com.automation.framework.base.BaseTest;
import com.automation.framework.moduledriver.LoginModule;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for login functionality
 */
public class LoginTest extends BaseTest {
    
    /**
     * Test valid login
     */
    @Test(description = "Test valid login with credentials from Excel")
    public void testValidLogin() {
        // Get test data from Excel
        String username = excelDataProvider.getTestData("LoginTest", "ValidUsername");
        String password = excelDataProvider.getTestData("LoginTest", "ValidPassword");
        
        // Create login module
        LoginModule loginModule = new LoginModule(driver, excelDataProvider);
        
        // Execute login steps
        loginModule.navigateToLoginPage()
                .login(username, password);
                
        // Assert login success (this is a placeholder - would need actual verification)
        String expectedTitle = excelDataProvider.getTestData("LoginTest", "DashboardTitle");
        Assert.assertEquals(driver.getTitle(), expectedTitle, "Dashboard title does not match expected");
    }
    
    /**
     * Test invalid login
     */
    @Test(description = "Test invalid login with credentials from Excel")
    public void testInvalidLogin() {
        // Get test data from Excel
        String username = excelDataProvider.getTestData("LoginTest", "InvalidUsername");
        String password = excelDataProvider.getTestData("LoginTest", "InvalidPassword");
        String expectedError = excelDataProvider.getTestData("LoginTest", "ErrorMessage");
        
        // Create login module
        LoginModule loginModule = new LoginModule(driver, excelDataProvider);
        
        // Execute login steps
        loginModule.navigateToLoginPage()
                .login(username, password);
                
        // Assert error message
        Assert.assertTrue(loginModule.verifyErrorMessage(expectedError), 
                "Error message not displayed or does not match expected");
    }
    
    /**
     * Test login with empty credentials
     */
    @Test(description = "Test login with empty credentials")
    public void testEmptyCredentials() {
        // Get test data from Excel
        String emptyUsername = "";
        String emptyPassword = "";
        String expectedError = excelDataProvider.getTestData("LoginTest", "EmptyCredentialsError");
        
        // Create login module
        LoginModule loginModule = new LoginModule(driver, excelDataProvider);
        
        // Execute login steps
        loginModule.navigateToLoginPage()
                .login(emptyUsername, emptyPassword);
                
        // Assert error message
        Assert.assertTrue(loginModule.verifyErrorMessage(expectedError), 
                "Error message not displayed or does not match expected");
    }
} 