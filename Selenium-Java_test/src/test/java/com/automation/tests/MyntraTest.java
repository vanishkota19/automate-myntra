package com.automation.tests;

import com.automation.framework.base.BaseTest;
import com.automation.framework.moduledriver.MyntraModule;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for Myntra operations
 */
public class MyntraTest extends BaseTest {
    
    /**
     * Test adding a product to the cart
     */
    @Test(description = "Test adding a product to the cart on Myntra")
    public void testAddProductToCart() {
        // Get test data from Excel
        String searchTerm = excelDataProvider.getTestData("testAddProductToCart", "searchTerm");
        String productIndexStr = excelDataProvider.getTestData("testAddProductToCart", "productIndex");
        
        // Use default values if Excel data is not available
        if (searchTerm == null || searchTerm.isEmpty()) {
            searchTerm = "men tshirt"; // Default search term
        }
        
        int productIndex = 1; // Default to first product
        if (productIndexStr != null && !productIndexStr.isEmpty()) {
            try {
                productIndex = Integer.parseInt(productIndexStr);
            } catch (NumberFormatException e) {
                // If parsing fails, use default value
            }
        }
        
        // Create Myntra module
        MyntraModule myntraModule = new MyntraModule(driver, excelDataProvider);
        
        // Execute Myntra shopping flow
        myntraModule.navigateToMyntra()
                .searchProduct(searchTerm)
                .selectProduct(productIndex) // Select the product from Excel data
                .selectSize()     // Select a size if available
                .addToCart()      // Add to cart
                .goToCart();      // Go to cart
                
        // Assert product is in cart
        Assert.assertTrue(myntraModule.verifyProductInCart(), "Product should be added to cart successfully");
    }
} 