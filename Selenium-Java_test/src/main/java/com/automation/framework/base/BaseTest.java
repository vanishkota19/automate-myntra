package com.automation.framework.base;

import com.automation.framework.utils.ExcelDataProvider;
import com.automation.framework.utils.ExtentReportManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Base Test class that all test classes will extend
 */
public class BaseTest {
    protected WebDriver driver;
    protected Properties config;
    protected ExcelDataProvider excelDataProvider;
    
    @BeforeSuite
    public void setUp() {
        config = loadConfig();
        ExtentReportManager.getInstance().initReports();
        excelDataProvider = new ExcelDataProvider();
    }
    
    @BeforeMethod
    @Parameters({"browser"})
    public void setUpTest(@Optional("chrome") String browser) {
        initializeDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        // Get URL from config or use default
        String url = config != null ? config.getProperty("app.url") : null;
        if (url == null || url.isEmpty()) {
            url = "https://www.myntra.com/"; // Default URL
        }
        driver.get(url);
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @AfterSuite
    public void tearDownSuite() {
        ExtentReportManager.getInstance().flushReports();
    }
    
    /**
     * Initialize WebDriver based on browser parameter
     * @param browser browser name
     */
    private void initializeDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
    }
    
    /**
     * Load configuration properties
     * @return Properties object
     */
    private Properties loadConfig() {
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            prop.load(fis);
            fis.close();
        } catch (Exception e) {
            System.out.println("Warning: Could not load config.properties. Using default values.");
            // Set default values
            prop.setProperty("app.url", "https://www.myntra.com/");
            prop.setProperty("implicit.wait", "10");
            prop.setProperty("explicit.wait", "20");
            prop.setProperty("page.load.timeout", "30");
            prop.setProperty("browser.headless", "false");
            prop.setProperty("browser.incognito", "false");
        }
        return prop;
    }
} 