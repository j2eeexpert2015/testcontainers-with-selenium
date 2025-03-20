package com.example.simple;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.example.utils.ScreenshotUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GoogleTitleTest {

    private static WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(GoogleTitleTest.class);

    @BeforeAll
    static void setup() {
        logger.info("Setting up WebDriver...");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        logger.info("WebDriver setup complete.");
    }

    @Test
    void testGoogleTitle() throws IOException {
        logger.info("Opening Google homepage...");
        driver.get("https://www.google.com");

        // Get the page title
        String pageTitle = driver.getTitle();
        logger.info("Page Title Retrieved: {}", pageTitle);

        // Validate title contains "Google"
        assertTrue(pageTitle.contains("Google"), "Page title does not contain 'Google'!");
        ScreenshotUtils.takeScreenshot(driver);
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed.");
        }
    }

    private String takeScreenshot() throws IOException {
        // Get the class and method name dynamically
        String className = this.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String fileName = className + "_" + methodName + ".png"; // Format: Class_Method.png

        // Define target directory for screenshots inside 'target/'
        String screenshotDir = System.getProperty("user.dir") + "/target/screenshots/";
        File destFile = new File(screenshotDir + fileName);

        // Ensure directory exists
        Files.createDirectories(destFile.getParentFile().toPath());

        // Delete existing file if it exists
        if (destFile.exists()) {
            Files.delete(destFile.toPath());
        }

        // Take and save screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(srcFile.toPath(), destFile.toPath());

        logger.info("Screenshot saved at: {}", destFile.getAbsolutePath());
        System.out.println("Screenshot saved at: " + destFile.getAbsolutePath());

        return destFile.getAbsolutePath(); // Return absolute path
    }


}
