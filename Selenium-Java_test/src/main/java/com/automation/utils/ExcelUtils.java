package com.automation.utils;

import com.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for Excel operations
 */
public class ExcelUtils {
    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);
    
    private ExcelUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Get data from Excel file as a list of maps
     * @param fileName Excel file name
     * @param sheetName Sheet name
     * @return List of maps with column names as keys and cell values as values
     */
    public static List<Map<String, String>> getExcelDataAsListOfMaps(String fileName, String sheetName) {
        List<Map<String, String>> data = new ArrayList<>();
        String filePath = ConfigReader.getInstance().getTestDataPath() + fileName;
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in file: " + filePath);
            }
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found in sheet: " + sheetName);
            }
            
            // Get column names
            List<String> columnNames = new ArrayList<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    columnNames.add(cell.getStringCellValue());
                } else {
                    columnNames.add("Column" + (i + 1));
                }
            }
            
            // Get data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int j = 0; j < columnNames.size(); j++) {
                        Cell cell = row.getCell(j);
                        String value = getCellValueAsString(cell);
                        rowData.put(columnNames.get(j), value);
                    }
                    data.add(rowData);
                }
            }
            
            logger.info("Read " + data.size() + " rows from Excel file: " + filePath + ", sheet: " + sheetName);
            return data;
            
        } catch (IOException e) {
            logger.error("Failed to read Excel file: " + e.getMessage());
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage());
        }
    }
    
    /**
     * Get data from Excel file as a 2D array
     * @param fileName Excel file name
     * @param sheetName Sheet name
     * @return 2D array of data
     */
    public static Object[][] getExcelDataAs2DArray(String fileName, String sheetName) {
        String filePath = ConfigReader.getInstance().getTestDataPath() + fileName;
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in file: " + filePath);
            }
            
            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();
            
            Object[][] data = new Object[rowCount][colCount];
            
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colCount; j++) {
                        Cell cell = row.getCell(j);
                        data[i - 1][j] = getCellValueAsString(cell);
                    }
                }
            }
            
            logger.info("Read " + rowCount + " rows from Excel file: " + filePath + ", sheet: " + sheetName);
            return data;
            
        } catch (IOException e) {
            logger.error("Failed to read Excel file: " + e.getMessage());
            throw new RuntimeException("Failed to read Excel file: " + e.getMessage());
        }
    }
    
    /**
     * Write data to Excel file
     * @param fileName Excel file name
     * @param sheetName Sheet name
     * @param data Data to write
     */
    public static void writeToExcel(String fileName, String sheetName, List<Map<String, String>> data) {
        String filePath = ConfigReader.getInstance().getTestDataPath() + fileName;
        File file = new File(filePath);
        
        try (Workbook workbook = file.exists() ? 
                WorkbookFactory.create(new FileInputStream(file)) : new XSSFWorkbook()) {
            
            // Create sheet if it doesn't exist
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                sheet = workbook.createSheet(sheetName);
            }
            
            // Get column names from first map
            if (!data.isEmpty()) {
                List<String> columnNames = new ArrayList<>(data.get(0).keySet());
                
                // Create header row
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    headerRow = sheet.createRow(0);
                }
                
                for (int i = 0; i < columnNames.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnNames.get(i));
                }
                
                // Create data rows
                for (int i = 0; i < data.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    Map<String, String> rowData = data.get(i);
                    
                    for (int j = 0; j < columnNames.size(); j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellValue(rowData.get(columnNames.get(j)));
                    }
                }
                
                // Auto-size columns
                for (int i = 0; i < columnNames.size(); i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            
            logger.info("Wrote " + data.size() + " rows to Excel file: " + filePath + ", sheet: " + sheetName);
            
        } catch (IOException e) {
            logger.error("Failed to write to Excel file: " + e.getMessage());
            throw new RuntimeException("Failed to write to Excel file: " + e.getMessage());
        }
    }
    
    /**
     * Get cell value as string
     * @param cell Cell to get value from
     * @return Cell value as string
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double value = cell.getNumericCellValue();
                    // Check if it's an integer
                    if (value == Math.floor(value)) {
                        return String.valueOf((long) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
} 