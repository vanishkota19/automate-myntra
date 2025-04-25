package com.automation.listeners;

import com.automation.config.ConfigReader;
import com.automation.core.DriverManager;
import com.automation.utils.ReportUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG listener for test execution events
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static boolean reportInitialized = false;
    
    @Override
    public void onStart(ITestContext context) {
        try {
            logger.info("Starting test suite: " + context.getName());
            if (!reportInitialized) {
                ReportUtils.initializeExtentReport();
                reportInitialized = true;
            }
        } catch (Exception e) {
            logger.error("Error in onStart: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void onFinish(ITestContext context) {
        try {
            logger.info("Finishing test suite: " + context.getName());
            ReportUtils.flushReport();
        } catch (Exception e) {
            logger.error("Error in onFinish: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        try {
            String testName = result.getMethod().getMethodName();
            String testDescription = result.getMethod().getDescription();
            if (testDescription == null) {
                testDescription = "Test case for " + testName;
            }
            
            logger.info("Starting test: " + testName);
            ReportUtils.createTest(testName, testDescription);
        } catch (Exception e) {
            logger.error("Error in onTestStart: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            logger.info("Test passed: " + result.getName());
            ReportUtils.logPass("Test passed: " + result.getName());
        } catch (Exception e) {
            logger.error("Error in onTestSuccess: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String testName = result.getName();
            String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";
            
            logger.error("Test failed: " + testName);
            logger.error("Exception: " + errorMessage);
            
            if (ConfigReader.getInstance().isTakeScreenshotOnFailure()) {
                String screenshotPath = captureScreenshot(testName);
                if (screenshotPath != null) {
                    ReportUtils.logFail("Test failed: " + errorMessage, screenshotPath);
                } else {
                    ReportUtils.logFail("Test failed: " + errorMessage);
                }
            } else {
                ReportUtils.logFail("Test failed: " + errorMessage);
            }
        } catch (Exception e) {
            logger.error("Error in onTestFailure: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            String testName = result.getName();
            String skipMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test skipped";
            
            logger.info("Test skipped: " + testName);
            ReportUtils.logSkip("Test skipped: " + skipMessage);
        } catch (Exception e) {
            logger.error("Error in onTestSkipped: " + e.getMessage(), e);
        }
    }
    
    /**
     * Capture screenshot
     * @param testName Test name
     * @return Path to screenshot
     */
    private String captureScreenshot(String testName) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String screenshotName = testName + "_" + timestamp + ".png";
                String screenshotDir = ConfigReader.getInstance().getScreenshotPath();
                
                File directory = new File(screenshotDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                
                String screenshotPath = screenshotDir + screenshotName;
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshotFile, new File(screenshotPath));
                
                logger.info("Screenshot captured: " + screenshotPath);
                return screenshotPath;
            }
        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error capturing screenshot: " + e.getMessage());
        }
        return null;
    }
} 