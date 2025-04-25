package com.automation.framework.listeners;

import com.automation.framework.utils.ExtentReportManager;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

/**
 * TestNG listener to handle test execution events
 */
public class TestListener implements ITestListener {
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("============ Starting Test Suite: " + context.getName() + " ============");
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("============ Finished Test Suite: " + context.getName() + " ============");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Starting test: " + result.getName());
        
        String className = result.getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        ExtentReportManager.getInstance().createTest(className, methodName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test passed: " + result.getName());
        ExtentReportManager.getInstance().getTest().log(Status.PASS, "Test passed");
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getName());
        
        // Log exception details
        Throwable t = result.getThrowable();
        ExtentReportManager.getInstance().getTest().log(Status.FAIL, "Test failed with exception: " + t.getMessage());
        ExtentReportManager.getInstance().getTest().log(Status.FAIL, Arrays.toString(t.getStackTrace()));
        
        // Capture screenshot if WebDriver is available
        try {
            Object testInstance = result.getInstance();
            Class<?> testClass = testInstance.getClass();
            
            try {
                java.lang.reflect.Field driverField = testClass.getSuperclass().getDeclaredField("driver");
                driverField.setAccessible(true);
                WebDriver driver = (WebDriver) driverField.get(testInstance);
                
                if (driver != null) {
                    String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    ExtentReportManager.getInstance().getTest().addScreenCaptureFromBase64String(base64Screenshot);
                }
            } catch (Exception e) {
                System.out.println("Could not take screenshot: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test skipped: " + result.getName());
        ExtentReportManager.getInstance().getTest().log(Status.SKIP, "Test skipped");
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test failed within success percentage: " + result.getName());
    }
} 