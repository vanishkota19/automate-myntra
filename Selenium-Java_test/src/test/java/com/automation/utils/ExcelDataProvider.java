package com.automation.utils;

import org.testng.annotations.DataProvider;

/**
 * Data provider class for Excel data
 */
public class ExcelDataProvider {
    
    /**
     * Data provider for Amazon search test
     * @return Object[][] with search data
     */
    @DataProvider(name = "amazonSearchData")
    public static Object[][] getAmazonSearchData() {
        return ExcelUtils.getExcelDataAs2DArray("TestData.xlsx", "AmazonSearch");
    }
    
    /**
     * Data provider for Amazon product test
     * @return Object[][] with product data
     */
    @DataProvider(name = "amazonProductData")
    public static Object[][] getAmazonProductData() {
        return ExcelUtils.getExcelDataAs2DArray("TestData.xlsx", "AmazonProduct");
    }
} 