package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page object for Amazon cart page
 */
public class AmazonCartPage extends BasePage {
    
    @FindBy(id = "sc-subtotal-amount-activecart")
    private WebElement subtotalAmount;
    
    @FindBy(css = "span.a-truncate-cut")
    private List<WebElement> productTitles;
    
    @FindBy(css = "input.a-color-link")
    private List<WebElement> deleteButtons;
    
    @FindBy(css = "input.a-button-input[name='proceedToRetailCheckout']")
    private WebElement proceedToCheckoutButton;
    
    @FindBy(css = "h1")
    private WebElement pageHeader;
    
    @FindBy(css = "div.sc-list-item")
    private List<WebElement> cartItems;
    
    /**
     * Constructor
     */
    public AmazonCartPage() {
        super();
        logger.info("Amazon Cart Page initialized");
    }
    
    /**
     * Get subtotal amount
     * @return Subtotal amount
     */
    public String getSubtotalAmount() {
        return getText(subtotalAmount).trim();
    }
    
    /**
     * Get number of items in cart
     * @return Number of items in cart
     */
    public int getNumberOfItemsInCart() {
        return cartItems.size();
    }
    
    /**
     * Check if cart is empty
     * @return true if cart is empty
     */
    public boolean isCartEmpty() {
        try {
            return getPageTitle().contains("Amazon.in Shopping Cart") && 
                   getText(pageHeader).contains("Your Amazon Cart is empty");
        } catch (Exception e) {
            return cartItems.isEmpty();
        }
    }
    
    /**
     * Get product titles in cart
     * @return List of product titles
     */
    public List<String> getProductTitles() {
        List<String> titles = new java.util.ArrayList<>();
        for (WebElement title : productTitles) {
            titles.add(getText(title));
        }
        return titles;
    }
    
    /**
     * Delete product by index
     * @param index Product index (0-based)
     */
    public void deleteProductByIndex(int index) {
        if (index >= 0 && index < deleteButtons.size()) {
            logger.info("Deleting product at index: " + index);
            click(deleteButtons.get(index));
        } else {
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
    }
    
    /**
     * Delete product by name
     * @param productName Product name
     */
    public void deleteProductByName(String productName) {
        logger.info("Deleting product: " + productName);
        
        List<String> titles = getProductTitles();
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).toLowerCase().contains(productName.toLowerCase())) {
                deleteProductByIndex(i);
                return;
            }
        }
        
        throw new RuntimeException("Product not found in cart: " + productName);
    }
    
    /**
     * Update product quantity
     * @param index Product index (0-based)
     * @param quantity New quantity
     */
    public void updateProductQuantity(int index, int quantity) {
        if (index >= 0 && index < cartItems.size()) {
            logger.info("Updating quantity of product at index " + index + " to " + quantity);
            
            WebElement quantityDropdown = cartItems.get(index).findElement(By.cssSelector("select.a-native-dropdown"));
            selectByVisibleText(quantityDropdown, String.valueOf(quantity));
        } else {
            throw new IllegalArgumentException("Invalid product index: " + index);
        }
    }
    
    /**
     * Update product quantity using dynamic locator
     * @param productName Product name
     * @param quantity New quantity
     */
    public void updateProductQuantityDynamic(String productName, int quantity) {
        logger.info("Updating quantity of product " + productName + " to " + quantity + " using dynamic locator");
        
        // Find the product row
        WebElement productRow = null;
        for (WebElement item : cartItems) {
            WebElement titleElement = item.findElement(By.cssSelector("span.a-truncate-cut"));
            String title = getText(titleElement);
            
            if (title.toLowerCase().contains(productName.toLowerCase())) {
                productRow = item;
                break;
            }
        }
        
        if (productRow != null) {
            WebElement quantityDropdown = productRow.findElement(By.cssSelector("select.a-native-dropdown"));
            selectByVisibleText(quantityDropdown, String.valueOf(quantity));
        } else {
            throw new RuntimeException("Product not found in cart: " + productName);
        }
    }
    
    /**
     * Proceed to checkout
     */
    public void proceedToCheckout() {
        logger.info("Proceeding to checkout");
        click(proceedToCheckoutButton);
    }
    
    /**
     * Proceed to checkout using dynamic locator
     */
    public void proceedToCheckoutDynamic() {
        logger.info("Proceeding to checkout using dynamic locator");
        clickByFieldName("Proceed to checkout");
    }
    
    /**
     * Verify cart page is loaded
     * @return true if cart page is loaded
     */
    public boolean isCartPageLoaded() {
        return getPageTitle().contains("Amazon.in Shopping Cart");
    }
} 