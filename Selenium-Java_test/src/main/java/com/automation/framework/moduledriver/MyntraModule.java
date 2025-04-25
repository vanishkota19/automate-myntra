package com.automation.framework.moduledriver;

import com.automation.framework.base.BasePage;
import com.automation.framework.utils.ExcelDataProvider;
import com.automation.framework.utils.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Module driver for Myntra product selection and cart operations
 */
public class MyntraModule extends BasePage {
    
    private ExcelDataProvider excelData;
    private final String PAGE_NAME = "MyntraPage";
    private WebDriverWait wait;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     * @param excelData ExcelDataProvider instance
     */
    public MyntraModule(WebDriver driver, ExcelDataProvider excelData) {
        super(driver);
        this.excelData = excelData;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    
    /**
     * Navigate to Myntra homepage
     * @return this instance for method chaining
     */
    public MyntraModule navigateToMyntra() {
        driver.get("https://www.myntra.com/");
        waitForPageLoad();
        
        safeLog(Status.INFO, "Navigated to Myntra homepage");
        return this;
    }
    
    /**
     * Search for a product
     * @param searchTerm product to search for
     * @return this instance for method chaining
     */
    public MyntraModule searchProduct(String searchTerm) {
        // Get XPath from Excel
        String searchBoxXpath = excelData.getXPath(PAGE_NAME, "searchBox");
        WebElement searchBox = findElementByXpath(searchBoxXpath);
        sendKeys(searchBox, searchTerm);
        
        // Press enter to search using Actions class
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).perform();
        waitForPageLoad();
        
        safeLog(Status.INFO, "Searched for product: " + searchTerm);
        return this;
    }
    
    /**
     * Select a product from search results (selects first product by default)
     * @param productIndex index of product to select (1-based)
     * @return this instance for method chaining
     */
    public MyntraModule selectProduct(int productIndex) {
        // Get XPath from Excel and replace index placeholder
        String productXpath = excelData.getXPath(PAGE_NAME, "productItem").replace("{index}", String.valueOf(productIndex));
        WebElement product = findElementByXpath(productXpath);
        
        // Scroll to element
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
        
        // Wait for element to be clickable and then click
        wait.until(ExpectedConditions.elementToBeClickable(product));
        click(product);
        
        // Handle new tab/window if product opens in one
        if (driver.getWindowHandles().size() > 1) {
            String originalWindow = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!originalWindow.equals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
        }
        
        waitForPageLoad();
        safeLog(Status.INFO, "Selected product #" + productIndex + " from search results");
        return this;
    }
    
    /**
     * Select a product size (if available)
     * @return this instance for method chaining
     */
    public MyntraModule selectSize() {
        try {
            // Get XPath from Excel
            String sizeXpath = excelData.getXPath(PAGE_NAME, "sizeOption");
            WebElement sizeElement = findElementByXpath(sizeXpath);
            click(sizeElement);
            safeLog(Status.INFO, "Selected size");
        } catch (Exception e) {
            safeLog(Status.INFO, "Size selection not required or available");
        }
        return this;
    }
    
    /**
     * Add current product to cart
     * @return this instance for method chaining
     */
    public MyntraModule addToCart() {
        try {
            // Get XPath from Excel
            String addToCartXpath = excelData.getXPath(PAGE_NAME, "addToCartButton");
            WebElement addToCartButton = findElementByXpath(addToCartXpath);
            
            // Wait for element to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
            
            // Scroll to element with more spacing to avoid overlapping elements
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addToCartButton);
            
            // Add a small wait to ensure the page settles after scrolling
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Use JavaScript click which bypasses element interception
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
            
            waitForPageLoad();
            
            safeLog(Status.INFO, "Added product to cart");
        } catch (Exception e) {
            safeLog(Status.FAIL, "Failed to add product to cart: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }
    
    /**
     * Navigate to cart
     * @return this instance for method chaining
     */
    public MyntraModule goToCart() {
        // Get XPath from Excel
        String cartIconXpath = excelData.getXPath(PAGE_NAME, "cartIcon");
        WebElement cartIcon = findElementByXpath(cartIconXpath);
        click(cartIcon);
        waitForPageLoad();
        
        safeLog(Status.INFO, "Navigated to cart");
        return this;
    }
    
    /**
     * Verify product is in cart
     * @return true if product appears in cart
     */
    public boolean verifyProductInCart() {
        // Get XPath from Excel
        String cartItemXpath = excelData.getXPath(PAGE_NAME, "cartItem");
        
        try {
            WebElement cartItem = findElementByXpath(cartItemXpath);
            boolean isPresent = cartItem.isDisplayed();
            
            if (isPresent) {
                safeLog(Status.PASS, "Product successfully added to cart");
            } else {
                safeLog(Status.FAIL, "Product not found in cart");
            }
            
            return isPresent;
        } catch (Exception e) {
            safeLog(Status.FAIL, "Failed to verify product in cart: " + e.getMessage());
            return false;
        }
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