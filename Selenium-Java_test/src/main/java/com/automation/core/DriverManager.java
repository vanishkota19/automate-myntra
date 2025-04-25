package com.automation.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages WebDriver instances for the framework
 */
public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    private DriverManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Initialize the WebDriver based on browser type
     * @param browser Browser type (chrome, firefox, edge, safari)
     * @param headless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver initializeDriver(String browser, boolean headless) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                
                // For downloading files
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", System.getProperty("user.dir") + "/downloads");
                chromeOptions.setExperimentalOption("prefs", prefs);
                
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                driver = new SafariDriver();
                break;
                
            default:
                throw new IllegalArgumentException("Browser " + browser + " is not supported");
        }
        
        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        
        // Maximize window
        driver.manage().window().maximize();
        
        // Set driver in ThreadLocal
        setDriver(driver);
        
        return driver;
    }
    
    /**
     * Get the current WebDriver instance
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Set the WebDriver instance
     * @param driver WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }
    
    /**
     * Register a WebDriverListener with the driver
     * @param listener WebDriverListener to register
     */
    public static void registerListener(WebDriverListener listener) {
        WebDriver original = getDriver();
        WebDriver decorated = new EventFiringDecorator(listener).decorate(original);
        setDriver(decorated);
    }
    
    /**
     * Quit the WebDriver instance and remove it from ThreadLocal
     */
    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
        }
    }
} 