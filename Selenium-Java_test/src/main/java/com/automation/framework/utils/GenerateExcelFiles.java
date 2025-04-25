package com.automation.framework.utils;

/**
 * Main class to generate Excel files
 */
public class GenerateExcelFiles {
    
    public static void main(String[] args) {
        System.out.println("Generating Excel files for test data and XPath...");
        ExcelFileGenerator.generateXPathRepo();
        ExcelFileGenerator.generateTestData();
        System.out.println("Excel files generated successfully!");
    }
} 