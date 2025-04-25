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

/**
 * Module driver for Myntra product selection and cart operations
 */
public class MyntraModule extends BasePage {
    
    private ExcelDataProvider excelData;
    private final String PAGE_NAME = "MyntraPage";
    
    // Hardcoded XPaths for Myntra elements
    private static final String SEARCH_BOX_XPATH = "//input[@class='desktop-searchBar' or @placeholder='Search for products, brands and more']";
    private static final String PRODUCT_ITEM_BASE_XPATH = "(//li[contains(@class, 'product-base')])";
    private static final String SIZE_OPTION_XPATH = "//button[contains(@class, 'size-buttons-size-button') or contains(@class, 'size-variant-button')][1]";
    private static final String ADD_TO_CART_BUTTON_XPATH = "//div[contains(@class,'pdp-add-to-bag') or @class='btn-gold' or contains(text(),'ADD TO BAG')]";
    private static final String CART_ICON_XPATH = "//span[contains(@class,'myntraweb-sprite desktop-iconBag')]";
    private static final String CART_ITEM_XPATH = "//div[contains(@class,'itemContainer')]";
    
    /**
     * Constructor
     * @param driver WebDriver instance
     * @param excelData ExcelDataProvider instance
     */
    public MyntraModule(WebDriver driver, ExcelDataProvider excelData) {
        super(driver);
        this.excelData = excelData;
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
        // Use hardcoded XPath instead of getting from Excel
        WebElement searchBox = findElementByXpath(SEARCH_BOX_XPATH);
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
        // Use hardcoded XPath with index
        String productXpath = PRODUCT_ITEM_BASE_XPATH + "[" + productIndex + "]";
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
            // Use hardcoded XPath
            WebElement sizeElement = findElementByXpath(SIZE_OPTION_XPATH);
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
            // Use hardcoded XPath
            WebElement addToCartButton = findElementByXpath(ADD_TO_CART_BUTTON_XPATH);
            
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
        // Use hardcoded XPath
        WebElement cartIcon = findElementByXpath(CART_ICON_XPATH);
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
        // Use hardcoded XPath
        try {
            WebElement cartItem = findElementByXpath(CART_ITEM_XPATH);
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