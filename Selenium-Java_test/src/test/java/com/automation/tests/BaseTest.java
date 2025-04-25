package com.automation.tests;

import com.automation.config.ConfigReader;
import com.automation.core.DriverManager;
import com.automation.utils.ReportUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.time.Duration;

/**
 * Base class for all test classes
 */
public class BaseTest {
    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected ConfigReader configReader = ConfigReader.getInstance();
    
    /**
     * Set up method to be run before each test class
     * @param browser Browser to use
     */
    @Parameters({"browser"})
    @BeforeClass(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {
        try {
            logger.info("Setting up test with browser: " + browser);
            driver = DriverManager.initializeDriver(browser, configReader.isHeadless());
            
            // Set implicit wait using Duration
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            // Maximize window
            driver.manage().window().maximize();
        } catch (Exception e) {
            logger.error("Error setting up test: " + e.getMessage(), e);
            throw new RuntimeException("Failed to set up test environment", e);
        }
    }
    
    /**
     * Navigate to base URL
     */
    @BeforeMethod(alwaysRun = true)
    public void navigateToBaseUrl() {
        try {
            String baseUrl = configReader.getBaseUrl();
            logger.info("Navigating to base URL: " + baseUrl);
            driver.get(baseUrl);
            
            // Wait for page to load completely
            waitForPageLoad(30);
        } catch (Exception e) {
            logger.error("Error navigating to base URL: " + e.getMessage(), e);
        }
    }
    
    /**
     * Log test start
     * @param method Test method name
     */
    @BeforeMethod(alwaysRun = true, dependsOnMethods = "navigateToBaseUrl")
    public void logTestStart(java.lang.reflect.Method method) {
        try {
            logger.info("Starting test: " + method.getName());
            ReportUtils.logInfo("Starting test: " + method.getName());
        } catch (Exception e) {
            logger.error("Error logging test start: " + e.getMessage(), e);
        }
    }
    
    /**
     * Log test end
     * @param method Test method name
     */
    @AfterMethod(alwaysRun = true)
    public void logTestEnd(java.lang.reflect.Method method) {
        try {
            logger.info("Finished test: " + method.getName());
            ReportUtils.logInfo("Finished test: " + method.getName());
        } catch (Exception e) {
            logger.error("Error logging test end: " + e.getMessage(), e);
        }
    }
    
    /**
     * Tear down method to be run after each test class
     */
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        try {
            logger.info("Tearing down test");
            DriverManager.quitDriver();
        } catch (Exception e) {
            logger.error("Error tearing down test: " + e.getMessage(), e);
        }
    }
    
    /**
     * Wait for page to load completely using a simple polling approach
     * @param timeoutInSeconds Timeout in seconds
     */
    protected void waitForPageLoad(int timeoutInSeconds, WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Poll for page load status
            for (int i = 0; i < timeoutInSeconds; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Check if page is loaded
                if (js.executeScript("return document.readyState").toString().equals("complete")) {
                    logger.debug("Page loaded completely");
                    return;
                }
            }
            
            logger.warn("Page load timeout after " + timeoutInSeconds + " seconds");
        } catch (Exception e) {
            logger.warn("Error waiting for page load: " + e.getMessage());
        }
    }
} 