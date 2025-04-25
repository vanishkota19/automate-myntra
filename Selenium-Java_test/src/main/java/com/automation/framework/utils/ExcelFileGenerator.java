package com.automation.framework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility class to generate Excel files for test data and XPath
 */
public class ExcelFileGenerator {
    
    /**
     * Generate XPathRepo.xlsx file
     */
    public static void generateXPathRepo() {
        String filePath = "src/test/resources/testdata/XPathRepo.xlsx";
        
        // Create directory if it doesn't exist
        File directory = new File(filePath).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create MyntraPage sheet
            Sheet myntraSheet = workbook.createSheet("MyntraPage");
            
            // Create header row
            Row headerRow = myntraSheet.createRow(0);
            headerRow.createCell(0).setCellValue("ElementName");
            headerRow.createCell(1).setCellValue("XPath");
            
            // Add Myntra XPaths
            int rowNum = 1;
            
            // Search box
            Row row = myntraSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("searchBox");
            row.createCell(1).setCellValue("//input[@class='desktop-searchBar' or @placeholder='Search for products, brands and more']");
            
            // Product item
            row = myntraSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("productItem");
            row.createCell(1).setCellValue("(//li[contains(@class, 'product-base')])[{index}]");
            
            // Size option
            row = myntraSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("sizeOption");
            row.createCell(1).setCellValue("//button[contains(@class, 'size-buttons-size-button') or contains(@class, 'size-variant-button')][1]");
            
            // Add to cart button
            row = myntraSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("addToCartButton");
            row.createCell(1).setCellValue("//div[contains(@class,'pdp-add-to-bag') or @class='btn-gold' or contains(text(),'ADD TO BAG')]");
            
            // Cart icon
            row = myntraSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("cartIcon");
            row.createCell(1).setCellValue("//span[contains(@class,'myntraweb-sprite desktop-iconBag')]");
            
            // Cart item
            row = myntraSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("cartItem");
            row.createCell(1).setCellValue("//div[contains(@class,'itemContainer')]");
            
            // Create LoginPage sheet
            Sheet loginSheet = workbook.createSheet("LoginPage");
            
            // Create header row
            headerRow = loginSheet.createRow(0);
            headerRow.createCell(0).setCellValue("ElementName");
            headerRow.createCell(1).setCellValue("XPath");
            
            // Add Login XPaths
            rowNum = 1;
            
            // Username field
            row = loginSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("usernameField");
            row.createCell(1).setCellValue("//input[@id='username' or @name='username']");
            
            // Password field
            row = loginSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("passwordField");
            row.createCell(1).setCellValue("//input[@id='password' or @name='password' or @type='password']");
            
            // Login button
            row = loginSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("loginButton");
            row.createCell(1).setCellValue("//button[@id='login-button' or contains(text(),'Login') or @type='submit']");
            
            // Error message
            row = loginSheet.createRow(rowNum++);
            row.createCell(0).setCellValue("errorMessage");
            row.createCell(1).setCellValue("//div[@class='error-message' or @id='error-message' or contains(@class,'error')]");
            
            // Auto-size columns
            for (int i = 0; i < 2; i++) {
                myntraSheet.autoSizeColumn(i);
                loginSheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            
            System.out.println("XPathRepo.xlsx created successfully at " + filePath);
            
        } catch (IOException e) {
            System.out.println("Error creating XPathRepo.xlsx: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generate TestData.xlsx file
     */
    public static void generateTestData() {
        String filePath = "src/test/resources/testdata/TestData.xlsx";
        
        // Create directory if it doesn't exist
        File directory = new File(filePath).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create TestData sheet
            Sheet sheet = workbook.createSheet("TestData");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("TestName");
            headerRow.createCell(1).setCellValue("ParameterName");
            headerRow.createCell(2).setCellValue("ParameterValue");
            
            // Add Myntra test data
            int rowNum = 1;
            
            // Test name
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("testAddProductToCart");
            row.createCell(1).setCellValue("searchTerm");
            row.createCell(2).setCellValue("men tshirt");
            
            // Product index
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("testAddProductToCart");
            row.createCell(1).setCellValue("productIndex");
            row.createCell(2).setCellValue("1");
            
            // Add Login test data
            // Login URL
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("LoginURL");
            row.createCell(2).setCellValue("https://www.myntra.com/login");
            
            // Valid username
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("ValidUsername");
            row.createCell(2).setCellValue("testuser@example.com");
            
            // Valid password
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("ValidPassword");
            row.createCell(2).setCellValue("Test@123");
            
            // Invalid username
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("InvalidUsername");
            row.createCell(2).setCellValue("invalid@example.com");
            
            // Invalid password
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("InvalidPassword");
            row.createCell(2).setCellValue("WrongPass");
            
            // Error message
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("ErrorMessage");
            row.createCell(2).setCellValue("Invalid email or password");
            
            // Empty credentials error
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("EmptyCredentialsError");
            row.createCell(2).setCellValue("Please enter your email and password");
            
            // Dashboard title
            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("LoginTest");
            row.createCell(1).setCellValue("DashboardTitle");
            row.createCell(2).setCellValue("Myntra - Online Shopping for Men, Women, Kids Fashion & Lifestyle");
            
            // Auto-size columns
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            
            System.out.println("TestData.xlsx created successfully at " + filePath);
            
        } catch (IOException e) {
            System.out.println("Error creating TestData.xlsx: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Main method to generate both Excel files
     */
    public static void main(String[] args) {
        generateXPathRepo();
        generateTestData();
    }
} 