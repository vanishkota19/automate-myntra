package com.automation.utils;

import com.automation.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utility class for waits
 */
public class WaitUtils {
    private static final int DEFAULT_TIMEOUT = 10;
    
    private WaitUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Get WebDriverWait instance with default timeout
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait() {
        return getWait(DEFAULT_TIMEOUT);
    }
    
    /**
     * Get WebDriverWait instance with custom timeout
     * @param timeoutInSeconds Timeout in seconds
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }
    
    /**
     * Wait for element to be visible
     * @param element WebElement to wait for
     * @return WebElement that is now visible
     */
    public static WebElement waitForElementToBeVisible(WebElement element) {
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be visible with custom timeout
     * @param element WebElement to wait for
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement that is now visible
     */
    public static WebElement waitForElementToBeVisible(WebElement element, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be clickable
     * @param element WebElement to wait for
     * @return WebElement that is now clickable
     */
    public static WebElement waitForElementToBeClickable(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Wait for element to be clickable with custom timeout
     * @param element WebElement to wait for
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement that is now clickable
     */
    public static WebElement waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Wait for element to be present
     * @param locator By locator
     * @return WebElement that is now present
     */
    public static WebElement waitForElementToBePresent(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be present with custom timeout
     * @param locator By locator
     * @param timeoutInSeconds Timeout in seconds
     * @return WebElement that is now present
     */
    public static WebElement waitForElementToBePresent(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for page to load completely
     */
    public static void waitForPageToLoad() {
        WebDriver driver = DriverManager.getDriver();
        getWait().until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Wait for AJAX calls to complete
     */
    public static void waitForAjaxToComplete() {
        WebDriver driver = DriverManager.getDriver();
        getWait().until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return jQuery.active == 0"));
    }
    
    /**
     * Sleep for specified time
     * @param milliseconds Time to sleep in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 