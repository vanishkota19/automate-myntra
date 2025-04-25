package com.automation.pages;

import com.automation.core.DriverManager;
import com.automation.locators.DynamicLocatorFactory;
import com.automation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base class for all Page Objects
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor js;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected DynamicLocatorFactory locatorFactory;
    
    /**
     * Constructor
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        this.locatorFactory = new DynamicLocatorFactory();
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Get page title
     * @return Page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    public void navigateTo(String url) {
        driver.get(url);
        logger.info("Navigated to URL: " + url);
    }
    
    /**
     * Refresh page
     */
    public void refreshPage() {
        driver.navigate().refresh();
        logger.info("Page refreshed");
    }
    
    /**
     * Click element
     * @param element WebElement to click
     */
    protected void click(WebElement element) {
        try {
            WaitUtils.waitForElementToBeClickable(element);
            element.click();
            logger.info("Clicked on element: " + element);
        } catch (Exception e) {
            logger.error("Failed to click element: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Click element using JavaScript
     * @param element WebElement to click
     */
    protected void clickUsingJS(WebElement element) {
        try {
            WaitUtils.waitForElementToBeVisible(element);
            js.executeScript("arguments[0].click();", element);
            logger.info("Clicked on element using JavaScript: " + element);
        } catch (Exception e) {
            logger.error("Failed to click element using JavaScript: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Type text into element
     * @param element WebElement to type into
     * @param text Text to type
     */
    protected void type(WebElement element, String text) {
        try {
            WaitUtils.waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed '" + text + "' into element: " + element);
        } catch (Exception e) {
            logger.error("Failed to type text: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get text from element
     * @param element WebElement to get text from
     * @return Text from element
     */
    protected String getText(WebElement element) {
        try {
            WaitUtils.waitForElementToBeVisible(element);
            String text = element.getText();
            logger.info("Got text from element: " + text);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Check if element is displayed
     * @param element WebElement to check
     * @return true if element is displayed
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Wait for element to be visible
     * @param element WebElement to wait for
     * @return WebElement that is now visible
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be clickable
     * @param element WebElement to wait for
     * @return WebElement that is now clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Select option by visible text
     * @param element Select element
     * @param visibleText Text to select
     */
    protected void selectByVisibleText(WebElement element, String visibleText) {
        try {
            WaitUtils.waitForElementToBeVisible(element);
            new Select(element).selectByVisibleText(visibleText);
            logger.info("Selected option with text '" + visibleText + "' from dropdown");
        } catch (Exception e) {
            logger.error("Failed to select by visible text: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Hover over element
     * @param element WebElement to hover over
     */
    protected void hoverOverElement(WebElement element) {
        try {
            WaitUtils.waitForElementToBeVisible(element);
            actions.moveToElement(element).perform();
            logger.info("Hovered over element: " + element);
        } catch (Exception e) {
            logger.error("Failed to hover over element: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Scroll to element
     * @param element WebElement to scroll to
     */
    protected void scrollToElement(WebElement element) {
        try {
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: " + element);
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Find element by field or button name
     * @param fieldName Field or button name
     * @return WebElement
     */
    protected WebElement findElementByFieldName(String fieldName) {
        return locatorFactory.findElementByFieldName(driver, fieldName);
    }
    
    /**
     * Click on a field or button by name
     * @param fieldName Field or button name
     */
    protected void clickByFieldName(String fieldName) {
        WebElement element = findElementByFieldName(fieldName);
        click(element);
        logger.info("Clicked on field/button: " + fieldName);
    }
    
    /**
     * Type text into a field by name
     * @param fieldName Field name
     * @param text Text to type
     */
    protected void typeByFieldName(String fieldName, String text) {
        WebElement element = findElementByFieldName(fieldName);
        type(element, text);
        logger.info("Typed '" + text + "' into field: " + fieldName);
    }
    
    /**
     * Take screenshot
     * @return Screenshot as byte array
     */
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
} 