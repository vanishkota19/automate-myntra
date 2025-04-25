package com.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page object for Amazon product page
 */
public class AmazonProductPage extends BasePage {
    
    @FindBy(id = "productTitle")
    private WebElement productTitle;
    
    @FindBy(id = "add-to-cart-button")
    private WebElement addToCartButton;
    
    @FindBy(id = "buy-now-button")
    private WebElement buyNowButton;
    
    @FindBy(id = "priceblock_ourprice")
    private WebElement productPrice;
    
    @FindBy(css = "span.a-size-base.a-color-success")
    private WebElement inStockStatus;
    
    @FindBy(css = "a#nav-cart")
    private WebElement cartIcon;
    
    @FindBy(css = "span#attach-sidesheet-checkout-button")
    private WebElement proceedToCheckoutButton;
    
    @FindBy(css = "span#attach-sidesheet-view-cart-button")
    private WebElement cartButton;
    
    /**
     * Constructor
     */
    public AmazonProductPage() {
        super();
        logger.info("Amazon Product Page initialized");
    }
    
    /**
     * Get product title
     * @return Product title
     */
    public String getProductTitle() {
        return getText(productTitle);
    }
    
    /**
     * Get product price
     * @return Product price
     */
    public String getProductPrice() {
        try {
            return getText(productPrice);
        } catch (Exception e) {
            logger.warn("Could not get product price using standard locator, trying alternative");
            // Try alternative locator
            WebElement altPrice = findElementByFieldName("price");
            return getText(altPrice);
        }
    }
    
    /**
     * Check if product is in stock
     * @return true if product is in stock
     */
    public boolean isProductInStock() {
        try {
            return getText(inStockStatus).contains("In stock");
        } catch (Exception e) {
            logger.warn("Could not determine stock status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add product to cart
     * @return AmazonProductPage
     */
    public AmazonProductPage addToCart() {
        logger.info("Adding product to cart");
        click(addToCartButton);
        return this;
    }
    
    /**
     * Add product to cart using dynamic locator
     * @return AmazonProductPage
     */
    public AmazonProductPage addToCartDynamic() {
        logger.info("Adding product to cart using dynamic locator");
        clickByFieldName("Add to Cart");
        return this;
    }
    
    /**
     * Proceed to checkout
     */
    public void proceedToCheckout() {
        logger.info("Proceeding to checkout");
        try {
            if (isElementDisplayed(proceedToCheckoutButton)) {
                click(proceedToCheckoutButton);
            } else {
                logger.info("Proceed to checkout button not found, trying dynamic locator");
                clickByFieldName("Proceed to checkout");
            }
        } catch (Exception e) {
            logger.error("Failed to proceed to checkout: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Navigate to cart
     * @return AmazonCartPage
     */
    public AmazonCartPage navigateToCart() {
        logger.info("Navigating to cart");
        try {
            if (isElementDisplayed(cartButton)) {
                click(cartButton);
            } else {
                click(cartIcon);
            }
            return new AmazonCartPage();
        } catch (Exception e) {
            logger.error("Failed to navigate to cart: " + e.getMessage());
            throw e;
        }
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
     * Verify product page is loaded
     * @return true if product page is loaded
     */
    public boolean isProductPageLoaded() {
        return isElementDisplayed(productTitle) && 
               getPageTitle().contains("Amazon.in");
    }
} 