package com.automation.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to manage ExtentReports
 */
public class ExtentReportManager {
    private static ExtentReportManager instance;
    private ExtentReports extent;
    private ExtentSparkReporter sparkReporter;
    private final Map<Long, ExtentTest> tests = new HashMap<>();
    private final Map<String, ExtentTest> classTests = new HashMap<>();
    
    private ExtentReportManager() {
        // Private constructor for singleton
    }
    
    /**
     * Gets the singleton instance
     * @return ExtentReportManager instance
     */
    public static synchronized ExtentReportManager getInstance() {
        if (instance == null) {
            instance = new ExtentReportManager();
        }
        return instance;
    }
    
    /**
     * Initialize reports
     */
    public void initReports() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String reportName = "TestReport_" + timeStamp + ".html";
        
        String reportsDir = "test-output" + File.separator + "reports";
        new File(reportsDir).mkdirs();
        
        String reportPath = reportsDir + File.separator + reportName;
        
        sparkReporter = new ExtentSparkReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Configure reporter
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Test Execution Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        
        // Set system info
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
    }
    
    /**
     * Create test in report
     * @param testName test name
     * @return ExtentTest object
     */
    public synchronized ExtentTest createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        tests.put(Thread.currentThread().getId(), test);
        return test;
    }
    
    /**
     * Create test in report with class name
     * @param className class name
     * @param testName test name
     * @return ExtentTest object
     */
    public synchronized ExtentTest createTest(String className, String testName) {
        ExtentTest classTest;
        
        if (!classTests.containsKey(className)) {
            classTest = extent.createTest(className);
            classTests.put(className, classTest);
        } else {
            classTest = classTests.get(className);
        }
        
        ExtentTest test = classTest.createNode(testName);
        tests.put(Thread.currentThread().getId(), test);
        return test;
    }
    
    /**
     * Get current test
     * @return current ExtentTest object
     */
    public synchronized ExtentTest getTest() {
        return tests.get(Thread.currentThread().getId());
    }
    
    /**
     * Flush reports
     */
    public void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
} 