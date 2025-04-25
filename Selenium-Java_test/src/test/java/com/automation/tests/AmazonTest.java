package com.automation.tests;

import com.automation.pages.AmazonCartPage;
import com.automation.pages.AmazonHomePage;
import com.automation.pages.AmazonProductPage;
import com.automation.pages.AmazonSearchResultsPage;
import com.automation.utils.ReportUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for Amazon workflow
 */
public class AmazonTest extends BaseTest {
    
    /**
     * Test search and add to cart functionality
     * @param searchKeyword Search keyword
     * @param productIndex Product index to select
     */
    @Test(description = "Test search and add to cart functionality")
    public void testSearchAndAddToCart() {
        String searchKeyword = "tshirt";
        int productIndex = 0;
        
        // Initialize home page
        AmazonHomePage homePage = new AmazonHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page is not loaded");
        ReportUtils.logPass("Home page loaded successfully");
        
        // Search for product
        AmazonSearchResultsPage searchResultsPage = homePage.searchProduct(searchKeyword);
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(), "Search results page is not loaded");
        ReportUtils.logPass("Search results page loaded successfully");
        
        // Verify search results
        int numberOfResults = searchResultsPage.getNumberOfSearchResults();
        Assert.assertTrue(numberOfResults > 0, "No search results found");
        ReportUtils.logPass("Found " + numberOfResults + " search results for '" + searchKeyword + "'");
        
        // Click on product
        AmazonProductPage productPage = searchResultsPage.clickOnProduct(productIndex);
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product page is not loaded");
        ReportUtils.logPass("Product page loaded successfully");
        
        // Get product title
        String productTitle = productPage.getProductTitle();
        ReportUtils.logInfo("Selected product: " + productTitle);
        
        // Add to cart
        productPage.addToCart();
        ReportUtils.logPass("Product added to cart successfully");
        
        // Navigate to cart
        AmazonCartPage cartPage = productPage.navigateToCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page is not loaded");
        ReportUtils.logPass("Cart page loaded successfully");
        
        // Verify product in cart
        int itemsInCart = cartPage.getNumberOfItemsInCart();
        Assert.assertTrue(itemsInCart > 0, "Cart is empty");
        ReportUtils.logPass("Cart contains " + itemsInCart + " item(s)");
        
        // Verify product title in cart
        boolean productFound = false;
        for (String title : cartPage.getProductTitles()) {
            if (productTitle.contains(title) || title.contains(productTitle)) {
                productFound = true;
                break;
            }
        }
        Assert.assertTrue(productFound, "Product not found in cart");
        ReportUtils.logPass("Product found in cart");
    }
    
    /**
     * Test search and add to cart functionality using dynamic locators
     */
    @Test(description = "Test search and add to cart functionality using dynamic locators")
    public void testSearchAndAddToCartDynamic() {
        String searchKeyword = "tshirt";
        
        // Initialize home page
        AmazonHomePage homePage = new AmazonHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page is not loaded");
        ReportUtils.logPass("Home page loaded successfully");
        
        // Search for product using dynamic locator
        AmazonSearchResultsPage searchResultsPage = homePage.searchProductDynamic(searchKeyword);
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(), "Search results page is not loaded");
        ReportUtils.logPass("Search results page loaded successfully");
        
        // Verify search results
        int numberOfResults = searchResultsPage.getNumberOfSearchResults();
        Assert.assertTrue(numberOfResults > 0, "No search results found");
        ReportUtils.logPass("Found " + numberOfResults + " search results for '" + searchKeyword + "'");
        
        // Click on first product that contains "tshirt" in the title
        AmazonProductPage productPage = searchResultsPage.clickOnProductByName("tshirt");
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product page is not loaded");
        ReportUtils.logPass("Product page loaded successfully");
        
        // Get product title
        String productTitle = productPage.getProductTitle();
        ReportUtils.logInfo("Selected product: " + productTitle);
        
        // Add to cart using dynamic locator
        productPage.addToCartDynamic();
        ReportUtils.logPass("Product added to cart successfully");
        
        // Navigate to cart using dynamic locator
        AmazonCartPage cartPage = productPage.navigateToCartDynamic();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page is not loaded");
        ReportUtils.logPass("Cart page loaded successfully");
        
        // Verify product in cart
        int itemsInCart = cartPage.getNumberOfItemsInCart();
        Assert.assertTrue(itemsInCart > 0, "Cart is empty");
        ReportUtils.logPass("Cart contains " + itemsInCart + " item(s)");
    }
    
    /**
     * Data provider for search and add to cart test
     * @return Object[][] with search keyword and product index
     */
    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return new Object[][] {
            {"tshirt", 0},
            {"shoes", 1},
            {"watch", 2}
        };
    }
    
    /**
     * Data-driven test for search and add to cart functionality
     * @param searchKeyword Search keyword
     * @param productIndex Product index to select
     */
    @Test(dataProvider = "searchData", description = "Data-driven test for search and add to cart functionality")
    public void testSearchAndAddToCartDataDriven(String searchKeyword, int productIndex) {
        // Initialize home page
        AmazonHomePage homePage = new AmazonHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page is not loaded");
        ReportUtils.logPass("Home page loaded successfully");
        
        // Search for product
        AmazonSearchResultsPage searchResultsPage = homePage.searchProduct(searchKeyword);
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(), "Search results page is not loaded");
        ReportUtils.logPass("Search results page loaded successfully");
        
        // Verify search results
        int numberOfResults = searchResultsPage.getNumberOfSearchResults();
        Assert.assertTrue(numberOfResults > 0, "No search results found");
        ReportUtils.logPass("Found " + numberOfResults + " search results for '" + searchKeyword + "'");
        
        // Click on product
        AmazonProductPage productPage = searchResultsPage.clickOnProduct(productIndex);
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product page is not loaded");
        ReportUtils.logPass("Product page loaded successfully");
        
        // Get product title
        String productTitle = productPage.getProductTitle();
        ReportUtils.logInfo("Selected product: " + productTitle);
        
        // Add to cart
        productPage.addToCart();
        ReportUtils.logPass("Product added to cart successfully");
        
        // Navigate to cart
        AmazonCartPage cartPage = productPage.navigateToCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page is not loaded");
        ReportUtils.logPass("Cart page loaded successfully");
        
        // Verify product in cart
        int itemsInCart = cartPage.getNumberOfItemsInCart();
        Assert.assertTrue(itemsInCart > 0, "Cart is empty");
        ReportUtils.logPass("Cart contains " + itemsInCart + " item(s)");
    }
} 