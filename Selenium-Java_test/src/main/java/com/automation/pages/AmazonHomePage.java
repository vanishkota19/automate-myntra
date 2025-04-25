package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for Amazon home page
 */
public class AmazonHomePage extends BasePage {
    
    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;
    
    @FindBy(id = "nav-search-submit-button")
    private WebElement searchButton;
    
    @FindBy(id = "nav-link-accountList")
    private WebElement accountAndLists;
    
    @FindBy(id = "nav-cart")
    private WebElement cartIcon;
    
    @FindBy(id = "nav-logo")
    private WebElement amazonLogo;
    
    /**
     * Constructor
     */
    public AmazonHomePage() {
        super();
        logger.info("Amazon Home Page initialized");
    }
    
    /**
     * Search for a product
     * @param productName Product name to search for
     * @return AmazonSearchResultsPage
     */
    public AmazonSearchResultsPage searchProduct(String productName) {
        logger.info("Searching for product: " + productName);
        type(searchBox, productName);
        click(searchButton);
        return new AmazonSearchResultsPage();
    }
    
    /**
     * Search for a product using dynamic locator
     * @param productName Product name to search for
     * @return AmazonSearchResultsPage
     */
    public AmazonSearchResultsPage searchProductDynamic(String productName) {
        logger.info("Searching for product using dynamic locator: " + productName);
        typeByFieldName("Search Amazon.in", productName);
        clickByFieldName("Go");
        return new AmazonSearchResultsPage();
    }
    
    /**
     * Navigate to cart
     * @return AmazonCartPage
     */
    public AmazonCartPage navigateToCart() {
        logger.info("Navigating to cart");
        click(cartIcon);
        return new AmazonCartPage();
    }
    
    /**
     * Navigate to cart using dynamic locator
     * @return AmazonCartPage
     */
    public AmazonCartPage navigateToCartDynamic() {
        logger.info("Navigating to cart using dynamic locator");
        clickByFieldName("Cart");
        return new AmazonCartPage();
    }
    
    /**
     * Hover over account and lists
     */
    public void hoverOverAccountAndLists() {
        logger.info("Hovering over Account & Lists");
        hoverOverElement(accountAndLists);
    }
    
    /**
     * Verify home page is loaded
     * @return true if home page is loaded
     */
    public boolean isHomePageLoaded() {
        try {
            // Wait for critical elements with timeout
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Check for title
            boolean titleCheck = false;
            try {
                wait.until(ExpectedConditions.titleContains("Amazon.in"));
                titleCheck = true;
            } catch (TimeoutException e) {
                logger.warn("Title check failed: " + e.getMessage());
            }
            
            // Check for search box
            boolean searchBoxCheck = false;
            try {
                wait.until(ExpectedConditions.visibilityOf(searchBox));
                searchBoxCheck = true;
            } catch (TimeoutException e) {
                logger.warn("Search box check failed: " + e.getMessage());
                // Try finding it again in case of stale element
                try {
                    WebElement searchBoxFresh = driver.findElement(By.id("twotabsearchtextbox"));
                    searchBoxCheck = isElementDisplayed(searchBoxFresh);
                } catch (Exception ex) {
                    logger.warn("Search box not found on retry: " + ex.getMessage());
                }
            }
            
            // Check for Amazon logo
            boolean logoCheck = false;
            try {
                logoCheck = isElementDisplayed(amazonLogo);
            } catch (Exception e) {
                logger.warn("Logo check failed: " + e.getMessage());
                // Try finding it with a different locator
                try {
                    WebElement altLogo = driver.findElement(By.cssSelector(".nav-logo-link"));
                    logoCheck = isElementDisplayed(altLogo);
                } catch (Exception ex) {
                    logger.warn("Logo not found with alternate locator: " + ex.getMessage());
                }
            }
            
            // Log the state of each check
            logger.info("Home page checks - Title contains Amazon.in: " + titleCheck + 
                       ", Search box visible: " + searchBoxCheck + 
                       ", Logo visible: " + logoCheck);
            
            // Return true if at least two of the checks pass
            return (titleCheck && searchBoxCheck) || (titleCheck && logoCheck) || (searchBoxCheck && logoCheck);
        } catch (Exception e) {
            logger.error("Error checking if home page is loaded: " + e.getMessage());
            return false;
        }
    }
} 