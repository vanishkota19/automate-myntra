package com.automation.framework.launcher;

import com.automation.framework.listeners.TestListener;
import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

/**
 * Launcher class to run tests programmatically
 */
public class TestRunner {
    
    public static void main(String[] args) {
        // Create an instance of TestNG
        TestNG testng = new TestNG();
        
        // Set test classes - using fully qualified class name to avoid import issues
        try {
            List<Class<?>> classes = Lists.newArrayList();
            Class<?> loginTestClass = Class.forName("com.automation.tests.LoginTest");
            classes.add(loginTestClass);
            testng.setTestClasses(classes.toArray(new Class[0]));
            
            // Set default parameters (will be used if not provided in XML)
            testng.addListener(new TestListener());
            
            // Run tests
            testng.run();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: LoginTest class not found. Make sure your package structure is correct.");
            e.printStackTrace();
        }
    }
} 