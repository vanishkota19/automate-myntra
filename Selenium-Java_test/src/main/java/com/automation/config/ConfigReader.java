package com.automation.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read configuration properties
 */
public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    private static ConfigReader instance;
    
    private ConfigReader() {
        loadProperties();
    }
    
    /**
     * Get singleton instance of ConfigReader
     * @return ConfigReader instance
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }
    
    /**
     * Load properties from config file
     */
    private void loadProperties() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config properties file: " + e.getMessage());
        }
    }
    
    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config file");
        }
        return value;
    }
    
    /**
     * Get property value by key with default value
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get browser name from config
     * @return Browser name
     */
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    /**
     * Check if headless mode is enabled
     * @return true if headless mode is enabled
     */
    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }
    
    /**
     * Get base URL from config
     * @return Base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    /**
     * Get implicit wait time in seconds
     * @return Implicit wait time
     */
    public int getImplicitWaitTime() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }
    
    /**
     * Get page load timeout in seconds
     * @return Page load timeout
     */
    public int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", "30"));
    }
    
    /**
     * Get script timeout in seconds
     * @return Script timeout
     */
    public int getScriptTimeout() {
        return Integer.parseInt(getProperty("script.timeout", "30"));
    }
    
    /**
     * Get test data path
     * @return Test data path
     */
    public String getTestDataPath() {
        return getProperty("test.data.path", "src/test/resources/testdata/");
    }
    
    /**
     * Get screenshot path
     * @return Screenshot path
     */
    public String getScreenshotPath() {
        return getProperty("screenshot.path", "target/screenshots/");
    }
    
    /**
     * Check if screenshots should be taken on failure
     * @return true if screenshots should be taken on failure
     */
    public boolean isTakeScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("take.screenshot.on.failure", "true"));
    }
    
    /**
     * Get Extent report path
     * @return Extent report path
     */
    public String getExtentReportPath() {
        return getProperty("extent.report.path", "target/reports/");
    }
    
    /**
     * Get Extent report name
     * @return Extent report name
     */
    public String getExtentReportName() {
        return getProperty("extent.report.name", "AutomationReport");
    }
} 