package com.automation.utils;

import com.automation.config.ConfigReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for JSON operations
 */
public class JsonUtils {
    private static final Logger logger = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private JsonUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Read JSON file and return as JsonNode
     * @param fileName JSON file name
     * @return JsonNode
     */
    public static JsonNode readJsonFile(String fileName) {
        String filePath = ConfigReader.getInstance().getTestDataPath() + fileName;
        
        try {
            File file = new File(filePath);
            return objectMapper.readTree(file);
        } catch (IOException e) {
            logger.error("Failed to read JSON file: " + e.getMessage());
            throw new RuntimeException("Failed to read JSON file: " + e.getMessage());
        }
    }
    
    /**
     * Get data from JSON file as a list of maps
     * @param fileName JSON file name
     * @param nodeName Node name
     * @return List of maps with JSON data
     */
    public static List<Map<String, Object>> getJsonDataAsListOfMaps(String fileName, String nodeName) {
        JsonNode rootNode = readJsonFile(fileName);
        JsonNode dataNode = rootNode.get(nodeName);
        
        if (dataNode == null || !dataNode.isArray()) {
            throw new RuntimeException("Node '" + nodeName + "' not found or is not an array in file: " + fileName);
        }
        
        List<Map<String, Object>> data = new ArrayList<>();
        
        for (JsonNode node : dataNode) {
            Map<String, Object> map = new HashMap<>();
            node.fields().forEachRemaining(entry -> map.put(entry.getKey(), getNodeValue(entry.getValue())));
            data.add(map);
        }
        
        logger.info("Read " + data.size() + " items from JSON file: " + fileName + ", node: " + nodeName);
        return data;
    }
    
    /**
     * Get data from JSON file as a 2D array
     * @param fileName JSON file name
     * @param nodeName Node name
     * @return 2D array of data
     */
    public static Object[][] getJsonDataAs2DArray(String fileName, String nodeName) {
        List<Map<String, Object>> data = getJsonDataAsListOfMaps(fileName, nodeName);
        Object[][] result = new Object[data.size()][1];
        
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        
        return result;
    }
    
    /**
     * Get value from JsonNode
     * @param node JsonNode
     * @return Object value
     */
    private static Object getNodeValue(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isInt()) {
            return node.asInt();
        } else if (node.isLong()) {
            return node.asLong();
        } else if (node.isDouble()) {
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isNull()) {
            return null;
        } else if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            node.forEach(item -> list.add(getNodeValue(item)));
            return list;
        } else if (node.isObject()) {
            Map<String, Object> map = new HashMap<>();
            node.fields().forEachRemaining(entry -> map.put(entry.getKey(), getNodeValue(entry.getValue())));
            return map;
        } else {
            return node.toString();
        }
    }
} 