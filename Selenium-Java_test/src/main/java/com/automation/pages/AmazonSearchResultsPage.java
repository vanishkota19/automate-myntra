package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page object for Amazon search results page
 */
public class AmazonSearchResultsPage extends BasePage {
    
    @FindBy(css = "div[data-component-type='s-search-result']")
    private List<WebElement> searchResults;
    
    @FindBy(css = "span.a-price-whole")
    private List<WebElement> productPrices;
    
    @FindBy(css = "span.a-color-state.a-text-bold")
    private WebElement searchKeyword;
    
    // Additional locators for more reliable page load verification
    @FindBy(css = ".s-breadcrumb")
    private WebElement breadcrumb;
    
    @FindBy(id = "s-result-count")
    private WebElement resultCount;
    
    /**
     * Constructor
     */
    public AmazonSearchResultsPage() {
        super();
        logger.info("Amazon Search Results Page initialized");
    }
    
    /**
     * Get number of search results
     * @return Number of search results
     */
    public int getNumberOfSearchResults() {
        return searchResults.size();
    }
    
    /**
     * Get search keyword
     * @return Search keyword
     */
    public String getSearchKeyword() {
        return getText(searchKeyword).replace("\"", "");
    }
    
    /**
     * Click on product by index
     * @param index Product index (0-based)
     * @return AmazonProductPage
     */
    public AmazonProductPage clickOnProduct(int index) {
        if (index >= 0 && index < searchResults.size()) {
            WebElement productTitle = searchResults.get(index).findElement(By.cssSelector("h2 a"));
            String productName = getText(productTitle);
            logger.info("Clicking on product: " + productName);
            click(productTitle);
            return new AmazonProductPage();
        } else {
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
    }
    
    /**
     * Click on product by name
     * @param productName Product name
     * @return AmazonProductPage
     */
    public AmazonProductPage clickOnProductByName(String productName) {
        logger.info("Looking for product with name: " + productName);
        
        for (WebElement result : searchResults) {
            WebElement titleElement = result.findElement(By.cssSelector("h2 a"));
            String title = getText(titleElement);
            
            if (title.toLowerCase().contains(productName.toLowerCase())) {
                logger.info("Found product: " + title);
                click(titleElement);
                return new AmazonProductPage();
            }
        }
        
        throw new RuntimeException("Product not found: " + productName);
    }
    
    /**
     * Click on product by name using dynamic locator
     * @param productName Product name
     * @return AmazonProductPage
     */
    public AmazonProductPage clickOnProductByNameDynamic(String productName) {
        logger.info("Looking for product with name using dynamic locator: " + productName);
        clickByFieldName(productName);
        return new AmazonProductPage();
    }
    
    /**
     * Get product price by index
     * @param index Product index (0-based)
     * @return Product price
     */
    public String getProductPrice(int index) {
        if (index >= 0 && index < productPrices.size()) {
            return getText(productPrices.get(index));
        } else {
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
    }
    
    /**
     * Filter results by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     */
    public void filterByPriceRange(String minPrice, String maxPrice) {
        logger.info("Filtering by price range: " + minPrice + " - " + maxPrice);
        
        // Find and fill min price field
        WebElement minPriceField = findElementByFieldName("Min");
        type(minPriceField, minPrice);
        
        // Find and fill max price field
        WebElement maxPriceField = findElementByFieldName("Max");
        type(maxPriceField, maxPrice);
        
        // Click Go button
        WebElement goButton = findElementByFieldName("Go");
        click(goButton);
    }
    
    /**
     * Verify search results page is loaded
     * @return true if search results page is loaded
     */
    public boolean isSearchResultsPageLoaded() {
        try {
            // Wait for page title to contain expected text
            boolean titleContainsAmazon = getPageTitle().contains("Amazon.in");
            
            // Check for any of the key elements that indicate search results page is loaded
            boolean hasSearchResults = false;
            boolean hasBreadcrumb = false;
            boolean hasResultCount = false;
            
            try {
                // Wait for search results to be present
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-component-type='s-search-result']")));
                hasSearchResults = !searchResults.isEmpty();
            } catch (TimeoutException | StaleElementReferenceException e) {
                logger.warn("Search results not found: " + e.getMessage());
            }
            
            try {
                hasBreadcrumb = isElementDisplayed(breadcrumb);
            } catch (Exception e) {
                logger.warn("Breadcrumb not found: " + e.getMessage());
            }
            
            try {
                hasResultCount = isElementDisplayed(resultCount);
            } catch (Exception e) {
                // Amazon sometimes uses different IDs for result count
                try {
                    WebElement altResultCount = driver.findElement(By.cssSelector(".a-section.a-spacing-small.a-spacing-top-small"));
                    hasResultCount = isElementDisplayed(altResultCount);
                } catch (Exception ex) {
                    logger.warn("Result count not found: " + ex.getMessage());
                }
            }
            
            // Log the state of each check
            logger.info("Search results page checks - Title contains Amazon: " + titleContainsAmazon + 
                       ", Has search results: " + hasSearchResults + 
                       ", Has breadcrumb: " + hasBreadcrumb + 
                       ", Has result count: " + hasResultCount);
            
            // Return true if title contains Amazon AND at least one of the other indicators is present
            return titleContainsAmazon && (hasSearchResults || hasBreadcrumb || hasResultCount);
        } catch (Exception e) {
            logger.error("Error checking if search results page is loaded: " + e.getMessage());
            return false;
        }
    }
} 