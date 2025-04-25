package com.automation.steps;

import com.automation.config.ConfigReader;
import com.automation.core.DriverManager;
import com.automation.pages.AmazonCartPage;
import com.automation.pages.AmazonHomePage;
import com.automation.pages.AmazonProductPage;
import com.automation.pages.AmazonSearchResultsPage;
import com.automation.utils.ReportUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Step definitions for Amazon feature
 */
public class AmazonSteps {
    private WebDriver driver;
    private AmazonHomePage homePage;
    private AmazonSearchResultsPage searchResultsPage;
    private AmazonProductPage productPage;
    private AmazonCartPage cartPage;
    private String productTitle;
    
    /**
     * Set up method to be run before each scenario
     * @param scenario Cucumber scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        ReportUtils.createTest(scenario.getName(), scenario.getId());
        driver = DriverManager.initializeDriver(ConfigReader.getInstance().getBrowser(), 
                                               ConfigReader.getInstance().isHeadless());
    }
    
    /**
     * Tear down method to be run after each scenario
     */
    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
    
    /**
     * Navigate to Amazon home page
     */
    @Given("I am on the Amazon home page")
    public void iAmOnTheAmazonHomePage() {
        driver.get(ConfigReader.getInstance().getBaseUrl());
        homePage = new AmazonHomePage();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page is not loaded");
        ReportUtils.logPass("Home page loaded successfully");
    }
    
    /**
     * Search for a product
     * @param product Product to search for
     */
    @When("I search for {string}")
    public void iSearchFor(String product) {
        searchResultsPage = homePage.searchProduct(product);
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(), "Search results page is not loaded");
        ReportUtils.logPass("Search results page loaded successfully");
    }
    
    /**
     * Search for a product using dynamic locator
     * @param product Product to search for
     */
    @When("I search for {string} using dynamic locator")
    public void iSearchForUsingDynamicLocator(String product) {
        searchResultsPage = homePage.searchProductDynamic(product);
        Assert.assertTrue(searchResultsPage.isSearchResultsPageLoaded(), "Search results page is not loaded");
        ReportUtils.logPass("Search results page loaded successfully using dynamic locator");
    }
    
    /**
     * Verify search results are displayed
     */
    @Then("I should see search results")
    public void iShouldSeeSearchResults() {
        int numberOfResults = searchResultsPage.getNumberOfSearchResults();
        Assert.assertTrue(numberOfResults > 0, "No search results found");
        ReportUtils.logPass("Found " + numberOfResults + " search results");
    }
    
    /**
     * Verify search results contain the search keyword
     * @param keyword Search keyword
     */
    @And("the search results should contain {string}")
    public void theSearchResultsShouldContain(String keyword) {
        String searchKeyword = searchResultsPage.getSearchKeyword();
        Assert.assertTrue(searchKeyword.toLowerCase().contains(keyword.toLowerCase()), 
                         "Search results do not contain the keyword: " + keyword);
        ReportUtils.logPass("Search results contain the keyword: " + keyword);
    }
    
    /**
     * Click on the first product in search results
     */
    @And("I click on the first product")
    public void iClickOnTheFirstProduct() {
        productPage = searchResultsPage.clickOnProduct(0);
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product page is not loaded");
        productTitle = productPage.getProductTitle();
        ReportUtils.logPass("Clicked on first product: " + productTitle);
    }
    
    /**
     * Click on a product containing the specified text in the title
     * @param text Text to search for in product title
     */
    @And("I click on a product containing {string} in the title")
    public void iClickOnAProductContainingInTheTitle(String text) {
        productPage = searchResultsPage.clickOnProductByName(text);
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product page is not loaded");
        productTitle = productPage.getProductTitle();
        ReportUtils.logPass("Clicked on product containing '" + text + "' in the title: " + productTitle);
    }
    
    /**
     * Verify product page is loaded
     */
    @Then("I should be on the product page")
    public void iShouldBeOnTheProductPage() {
        Assert.assertTrue(productPage.isProductPageLoaded(), "Product page is not loaded");
        ReportUtils.logPass("Product page loaded successfully");
    }
    
    /**
     * Add product to cart
     */
    @When("I add the product to cart")
    public void iAddTheProductToCart() {
        productPage.addToCart();
        ReportUtils.logPass("Product added to cart successfully");
    }
    
    /**
     * Add product to cart using dynamic locator
     */
    @When("I add the product to cart using dynamic locator")
    public void iAddTheProductToCartUsingDynamicLocator() {
        productPage.addToCartDynamic();
        ReportUtils.logPass("Product added to cart successfully using dynamic locator");
    }
    
    /**
     * Navigate to cart
     */
    @And("I navigate to the cart")
    public void iNavigateToTheCart() {
        cartPage = productPage.navigateToCart();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page is not loaded");
        ReportUtils.logPass("Cart page loaded successfully");
    }
    
    /**
     * Navigate to cart using dynamic locator
     */
    @And("I navigate to the cart using dynamic locator")
    public void iNavigateToTheCartUsingDynamicLocator() {
        cartPage = productPage.navigateToCartDynamic();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page is not loaded");
        ReportUtils.logPass("Cart page loaded successfully using dynamic locator");
    }
    
    /**
     * Verify product is in cart
     */
    @Then("the product should be in the cart")
    public void theProductShouldBeInTheCart() {
        int itemsInCart = cartPage.getNumberOfItemsInCart();
        Assert.assertTrue(itemsInCart > 0, "Cart is empty");
        ReportUtils.logPass("Cart contains " + itemsInCart + " item(s)");
        
        if (productTitle != null) {
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
    }
} 