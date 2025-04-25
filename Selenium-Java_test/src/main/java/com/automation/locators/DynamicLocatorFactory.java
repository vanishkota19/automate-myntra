package com.automation.locators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating dynamic locators based on field or button names
 */
public class DynamicLocatorFactory {
    private static final Logger logger = LogManager.getLogger(DynamicLocatorFactory.class);
    
    /**
     * Find element by field or button name
     * @param driver WebDriver instance
     * @param fieldName Field or button name
     * @return WebElement
     */
    public WebElement findElementByFieldName(WebDriver driver, String fieldName) {
        logger.info("Finding element by field name: " + fieldName);
        
        List<By> locators = createLocatorsForFieldName(fieldName);
        WebElement foundElement = null;
        
        // First try with explicit wait for better performance
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
        for (By locator : locators) {
            try {
                foundElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                if (isElementVisible(foundElement) && isElementInteractable(foundElement)) {
                    logger.info("Found element for field name '" + fieldName + "' using locator: " + locator);
                    return foundElement;
                }
            } catch (Exception e) {
                // Continue to next locator
            }
        }
        
        // If explicit wait didn't work, try the traditional approach
        for (By locator : locators) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (!elements.isEmpty()) {
                    for (WebElement element : elements) {
                        try {
                            if (isElementVisible(element) && isElementInteractable(element)) {
                                logger.info("Found element for field name '" + fieldName + "' using locator: " + locator);
                                return element;
                            }
                        } catch (StaleElementReferenceException e) {
                            // Element became stale, try to find it again
                            elements = driver.findElements(locator);
                            if (!elements.isEmpty()) {
                                element = elements.get(0);
                                if (isElementVisible(element) && isElementInteractable(element)) {
                                    logger.info("Found element for field name '" + fieldName + "' using locator: " + locator + " (after handling stale element)");
                                    return element;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.debug("Exception while finding element with locator " + locator + ": " + e.getMessage());
            }
        }
        
        // If we still haven't found the element, log all the locators we tried
        logger.error("Could not find element for field name: " + fieldName);
        logger.error("Tried the following locators:");
        for (By locator : locators) {
            logger.error(" - " + locator);
        }
        
        throw new RuntimeException("Could not find element for field name: " + fieldName);
    }
    
    /**
     * Create a list of possible locators for a field or button name
     * @param fieldName Field or button name
     * @return List of By locators
     */
    private List<By> createLocatorsForFieldName(String fieldName) {
        List<By> locators = new ArrayList<>();
        
        // Input fields
        locators.add(By.xpath("//input[@placeholder='" + fieldName + "']"));
        locators.add(By.xpath("//input[@name='" + fieldName + "']"));
        locators.add(By.xpath("//input[@id='" + fieldName + "']"));
        locators.add(By.xpath("//input[contains(@placeholder,'" + fieldName + "')]"));
        locators.add(By.xpath("//input[contains(@name,'" + fieldName + "')]"));
        locators.add(By.xpath("//input[contains(@id,'" + fieldName + "')]"));
        locators.add(By.xpath("//label[contains(text(),'" + fieldName + "')]/following::input[1]"));
        locators.add(By.xpath("//label[text()='" + fieldName + "']/following::input[1]"));
        
        // Buttons
        locators.add(By.xpath("//button[text()='" + fieldName + "']"));
        locators.add(By.xpath("//button[contains(text(),'" + fieldName + "')]"));
        locators.add(By.xpath("//button[@name='" + fieldName + "']"));
        locators.add(By.xpath("//button[@id='" + fieldName + "']"));
        locators.add(By.xpath("//button[@value='" + fieldName + "']"));
        locators.add(By.xpath("//input[@type='button' and @value='" + fieldName + "']"));
        locators.add(By.xpath("//input[@type='submit' and @value='" + fieldName + "']"));
        
        // Links
        locators.add(By.xpath("//a[text()='" + fieldName + "']"));
        locators.add(By.xpath("//a[contains(text(),'" + fieldName + "')]"));
        locators.add(By.xpath("//a[@aria-label='" + fieldName + "']"));
        locators.add(By.xpath("//a[contains(@aria-label,'" + fieldName + "')]"));
        
        // Spans and divs (for clickable elements)
        locators.add(By.xpath("//span[text()='" + fieldName + "']"));
        locators.add(By.xpath("//span[contains(text(),'" + fieldName + "')]"));
        locators.add(By.xpath("//div[text()='" + fieldName + "']"));
        locators.add(By.xpath("//div[contains(text(),'" + fieldName + "')]"));
        
        // Select elements
        locators.add(By.xpath("//select[@name='" + fieldName + "']"));
        locators.add(By.xpath("//select[@id='" + fieldName + "']"));
        locators.add(By.xpath("//label[contains(text(),'" + fieldName + "')]/following::select[1]"));
        
        // Checkboxes and radio buttons
        locators.add(By.xpath("//input[@type='checkbox' and @name='" + fieldName + "']"));
        locators.add(By.xpath("//input[@type='radio' and @name='" + fieldName + "']"));
        locators.add(By.xpath("//label[contains(text(),'" + fieldName + "')]/preceding::input[@type='checkbox'][1]"));
        locators.add(By.xpath("//label[contains(text(),'" + fieldName + "')]/preceding::input[@type='radio'][1]"));
        locators.add(By.xpath("//label[contains(text(),'" + fieldName + "')]/following::input[@type='checkbox'][1]"));
        locators.add(By.xpath("//label[contains(text(),'" + fieldName + "')]/following::input[@type='radio'][1]"));
        
        // Amazon specific locators
        locators.add(By.xpath("//input[@aria-label='" + fieldName + "']"));
        locators.add(By.xpath("//input[contains(@aria-label,'" + fieldName + "')]"));
        locators.add(By.xpath("//span[contains(@id,'" + fieldName.toLowerCase().replace(" ", "-") + "')]"));
        
        // Additional Amazon specific locators
        if (fieldName.equalsIgnoreCase("Search") || fieldName.contains("Search Amazon")) {
            locators.add(By.id("twotabsearchtextbox"));
            locators.add(By.xpath("//input[@type='text' and @id='twotabsearchtextbox']"));
        }
        
        if (fieldName.equalsIgnoreCase("Go") || fieldName.equalsIgnoreCase("Search") || 
            fieldName.equalsIgnoreCase("Submit")) {
            locators.add(By.id("nav-search-submit-button"));
            locators.add(By.xpath("//input[@type='submit' and @value='Go']"));
        }
        
        if (fieldName.equalsIgnoreCase("Cart")) {
            locators.add(By.id("nav-cart"));
            locators.add(By.xpath("//a[@id='nav-cart']"));
            locators.add(By.xpath("//span[@id='nav-cart-count']"));
        }
        
        if (fieldName.equalsIgnoreCase("Add to Cart") || fieldName.contains("Add to Cart")) {
            locators.add(By.id("add-to-cart-button"));
            locators.add(By.xpath("//input[@id='add-to-cart-button']"));
            locators.add(By.xpath("//span[contains(text(),'Add to Cart')]"));
        }
        
        // Case-insensitive search for text
        locators.add(By.xpath("//span[translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='" + fieldName.toLowerCase() + "']"));
        locators.add(By.xpath("//span[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + fieldName.toLowerCase() + "')]"));
        
        return locators;
    }
    
    /**
     * Check if element is visible
     * @param element WebElement to check
     * @return true if element is visible
     */
    private boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if element is interactable
     * @param element WebElement to check
     * @return true if element is interactable
     */
    private boolean isElementInteractable(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
} 