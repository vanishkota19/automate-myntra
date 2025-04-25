package com.automation.tests;

import com.automation.pages.AmazonCartPage;
import com.automation.pages.AmazonHomePage;
import com.automation.pages.AmazonProductPage;
import com.automation.pages.AmazonSearchResultsPage;
import com.automation.utils.ExcelUtils;
import com.automation.utils.ReportUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Excel data-driven test class
 */
public class ExcelDataDrivenTest extends BaseTest {
    
    /**
     * Data provider for Amazon search test using Excel
     * @return Object[][] with search data
     */
    @DataProvider(name = "excelSearchData")
    public Object[][] getExcelSearchData() {
        List<Map<String, String>> excelData = ExcelUtils.getExcelDataAsListOfMaps("TestData.xlsx", "AmazonSearch");
        Object[][] data = new Object[excelData.size()][1];
        
        for (int i = 0; i < excelData.size(); i++) {
            data[i][0] = excelData.get(i);
        }
        
        return data;
    }
    
    /**
     * Excel data-driven test for search and add to cart functionality
     * @param testData Test data from Excel
     */
    @Test(dataProvider = "excelSearchData", description = "Excel data-driven test for search and add to cart functionality")
    public void testSearchAndAddToCartWithExcelData(Map<String, String> testData) {
        String searchKeyword = testData.get("SearchKeyword");
        int productIndex = Integer.parseInt(testData.get("ProductIndex"));
        String expectedTitle = testData.get("ExpectedTitle");
        
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
        
        // Verify product title contains expected title
        Assert.assertTrue(productTitle.toLowerCase().contains(expectedTitle.toLowerCase()), 
                         "Product title does not contain expected title: " + expectedTitle);
        ReportUtils.logPass("Product title contains expected title: " + expectedTitle);
        
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