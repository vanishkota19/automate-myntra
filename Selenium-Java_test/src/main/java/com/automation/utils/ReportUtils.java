package com.automation.utils;

import com.automation.config.ConfigReader;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for Extent Reports
 */
public class ReportUtils {
    private static final Logger logger = LogManager.getLogger(ReportUtils.class);
    private static ExtentReports extentReports;
    private static final Map<Long, ExtentTest> testMap = new HashMap<>();
    private static final String DEFAULT_TEST_NAME = "Test";
    
    private ReportUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Initialize Extent Reports
     */
    public static synchronized void initializeExtentReport() {
        if (extentReports == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportDir = ConfigReader.getInstance().getExtentReportPath();
            String reportName = ConfigReader.getInstance().getExtentReportName() + "_" + timestamp + ".html";
            
            File directory = new File(reportDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String reportPath = reportDir + reportName;
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle(ConfigReader.getInstance().getProperty("extent.report.title", "Automation Report"));
            sparkReporter.config().setReportName(ConfigReader.getInstance().getExtentReportName());
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("utf-8");
            
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("Browser", ConfigReader.getInstance().getBrowser());
            extentReports.setSystemInfo("Environment", "QA");
            
            logger.info("Extent Report initialized at: " + reportPath);
        }
    }
    
    /**
     * Create a new test in Extent Reports
     * @param testName Test name
     * @param description Test description
     */
    public static synchronized void createTest(String testName, String description) {
        if (extentReports == null) {
            initializeExtentReport();
        }
        
        ExtentTest test = extentReports.createTest(testName, description);
        testMap.put(Thread.currentThread().getId(), test);
    }
    
    /**
     * Get the current test
     * @return ExtentTest instance
     */
    private static synchronized ExtentTest getTest() {
        Long threadId = Thread.currentThread().getId();
        ExtentTest test = testMap.get(threadId);
        
        // If test is null, create a default test to prevent NullPointerException
        if (test == null) {
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            test = extentReports.createTest(methodName != null ? methodName : DEFAULT_TEST_NAME);
            testMap.put(threadId, test);
            logger.warn("Created default test for thread: " + threadId + " as no test was found");
        }
        
        return test;
    }
    
    /**
     * Log a passed step
     * @param message Step message
     */
    public static void logPass(String message) {
        try {
            getTest().log(Status.PASS, message);
            logger.info("PASS: " + message);
        } catch (Exception e) {
            logger.error("Failed to log pass message: " + e.getMessage());
        }
    }
    
    /**
     * Log a failed step
     * @param message Step message
     */
    public static void logFail(String message) {
        try {
            getTest().log(Status.FAIL, message);
            logger.error("FAIL: " + message);
        } catch (Exception e) {
            logger.error("Failed to log fail message: " + e.getMessage());
        }
    }
    
    /**
     * Log a failed step with screenshot
     * @param message Step message
     * @param screenshotPath Path to screenshot
     */
    public static void logFail(String message, String screenshotPath) {
        try {
            getTest().fail(message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            logger.error("FAIL: " + message + " (Screenshot: " + screenshotPath + ")");
        } catch (Exception e) {
            logFail(message);
            logger.error("Failed to attach screenshot to report: " + e.getMessage());
        }
    }
    
    /**
     * Log a skipped step
     * @param message Step message
     */
    public static void logSkip(String message) {
        try {
            getTest().log(Status.SKIP, message);
            logger.info("SKIP: " + message);
        } catch (Exception e) {
            logger.error("Failed to log skip message: " + e.getMessage());
        }
    }
    
    /**
     * Log an info step
     * @param message Step message
     */
    public static void logInfo(String message) {
        try {
            getTest().log(Status.INFO, message);
            logger.info("INFO: " + message);
        } catch (Exception e) {
            logger.error("Failed to log info message: " + e.getMessage());
        }
    }
    
    /**
     * Log a warning step
     * @param message Step message
     */
    public static void logWarning(String message) {
        try {
            getTest().log(Status.WARNING, message);
            logger.warn("WARNING: " + message);
        } catch (Exception e) {
            logger.error("Failed to log warning message: " + e.getMessage());
        }
    }
    
    /**
     * Flush the report
     */
    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("Extent Report flushed");
        }
    }
} 