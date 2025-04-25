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
        // Get product search term from Excel (or use a default)
        String productSearchTerm = excelDataProvider.getTestData("MyntraTest", "ProductSearchTerm");
        if (productSearchTerm == null || productSearchTerm.startsWith("default_")) {
            productSearchTerm = "Men T-shirts"; // Default search term
        }
        
        // Create Myntra module
        MyntraModule myntraModule = new MyntraModule(driver, excelDataProvider);
        
        // Execute Myntra shopping flow
        myntraModule.navigateToMyntra()
                .searchProduct(productSearchTerm)
                .selectProduct(1) // Select the first product
                .selectSize()     // Select a size if available
                .addToCart()      // Add to cart
                .goToCart();      // Go to cart
                
        // Assert product is in cart
        Assert.assertTrue(myntraModule.verifyProductInCart(), "Product should be added to cart successfully");
    }
} 