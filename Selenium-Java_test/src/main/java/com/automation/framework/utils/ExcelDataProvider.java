package com.automation.framework.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to handle Excel data for test data and XPath
 */
public class ExcelDataProvider {
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/TestData.xlsx";
    private static final String XPATH_DATA_PATH = "src/test/resources/testdata/XPathRepo.xlsx";
    
    private Map<String, Map<String, String>> testDataCache = new HashMap<>();
    private Map<String, Map<String, String>> xpathCache = new HashMap<>();
    
    public ExcelDataProvider() {
        // Initialize caches
        loadTestData();
        loadXPathData();
    }
    
    /**
     * Get test data for a specific test case
     * @param testCase test case ID
     * @param field field name
     * @return value of the field
     */
    public String getTestData(String testCase, String field) {
        if (testDataCache.containsKey(testCase)) {
            return testDataCache.get(testCase).get(field);
        }
        
        // Return appropriate default values based on field name
        System.out.println("WARNING: Test data not found for " + testCase + "." + field + ". Returning default value.");
        
        // Handle special cases for specific fields
        if ("LoginURL".equals(field)) {
            return "https://example.com/login";
        } else if (field.contains("Username")) {
            return "defaultuser";
        } else if (field.contains("Password")) {
            return "defaultpass";
        } else if (field.contains("Error")) {
            return "Default error message";
        } else if ("DashboardTitle".equals(field)) {
            return "Dashboard";
        }
        
        // Default fallback
        return "default_" + field;
    }
    
    /**
     * Get xpath for a specific page and element
     * @param page page name
     * @param element element name
     * @return xpath expression
     */
    public String getXPath(String page, String element) {
        if (xpathCache.containsKey(page)) {
            return xpathCache.get(page).get(element);
        }
        // Return a default XPath if Excel file is not available
        System.out.println("WARNING: XPath not found for " + page + "." + element + ". Returning default xpath.");
        
        // Common default XPaths for typical elements
        if ("usernameField".equals(element)) {
            return "//input[@id='username' or @name='username']";
        } else if ("passwordField".equals(element)) {
            return "//input[@id='password' or @name='password' or @type='password']";
        } else if ("loginButton".equals(element)) {
            return "//button[@id='login-button' or contains(text(),'Login') or @type='submit']";
        } else if ("errorMessage".equals(element)) {
            return "//div[@class='error-message' or @id='error-message' or contains(@class,'error')]";
        }
        
        return "//default-xpath-for-" + element;
    }
    
    /**
     * Load all test data from Excel file
     */
    private void loadTestData() {
        File file = new File(TEST_DATA_PATH);
        if (!file.exists()) {
            System.out.println("WARNING: TestData.xlsx not found at " + TEST_DATA_PATH);
            System.out.println("Please create the Excel file as per README_TestData.md");
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(TEST_DATA_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet("TestData");
            if (sheet == null) return;
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return;
            
            // Process data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                String testCaseId = getCellValueAsString(row.getCell(0));
                if (testCaseId == null || testCaseId.isEmpty()) continue;
                
                Map<String, String> testCaseData = new HashMap<>();
                for (int j = 1; j < headerRow.getLastCellNum(); j++) {
                    String header = getCellValueAsString(headerRow.getCell(j));
                    if (header == null || header.isEmpty()) continue;
                    
                    String value = getCellValueAsString(row.getCell(j));
                    testCaseData.put(header, value);
                }
                testDataCache.put(testCaseId, testCaseData);
            }
            
        } catch (IOException e) {
            System.out.println("Error loading TestData.xlsx: " + e.getMessage());
        }
    }
    
    /**
     * Load all XPath data from Excel file
     */
    private void loadXPathData() {
        File file = new File(XPATH_DATA_PATH);
        if (!file.exists()) {
            System.out.println("WARNING: XPathRepo.xlsx not found at " + XPATH_DATA_PATH);
            System.out.println("Please create the Excel file as per README_XPathRepo.md");
            return;
        }
        
        try (FileInputStream fis = new FileInputStream(XPATH_DATA_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
                Sheet sheet = workbook.getSheetAt(s);
                String pageName = sheet.getSheetName();
                
                // Get header row
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) continue;
                
                Map<String, String> pageXPaths = new HashMap<>();
                
                // Process data rows
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    
                    String elementName = getCellValueAsString(row.getCell(0));
                    String xpath = getCellValueAsString(row.getCell(1));
                    
                    if (elementName != null && !elementName.isEmpty() && 
                        xpath != null && !xpath.isEmpty()) {
                        pageXPaths.put(elementName, xpath);
                    }
                }
                
                xpathCache.put(pageName, pageXPaths);
            }
            
        } catch (IOException e) {
            System.out.println("Error loading XPathRepo.xlsx: " + e.getMessage());
        }
    }
    
    /**
     * Get cell value as string regardless of cell type
     * @param cell Excel cell
     * @return string value
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numValue = cell.getNumericCellValue();
                // Check if it's an integer
                if (numValue == Math.floor(numValue)) {
                    return String.format("%.0f", numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return "";
                    }
                }
            default:
                return "";
        }
    }
} 