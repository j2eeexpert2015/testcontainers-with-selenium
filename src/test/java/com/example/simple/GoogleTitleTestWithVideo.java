package com.example.simple;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.utils.CustomScreenRecorder;
import com.example.utils.ScreenshotUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GoogleTitleTestWithVideo {

    private WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(GoogleTitleTestWithVideo.class);
    private CustomScreenRecorder screenRecorder; // Monte Media Screen Recorder
    private String videoFilePath; // Store video file path

    @BeforeAll
    void setup() throws Exception {
        logger.info("Setting up WebDriver...");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        logger.info("WebDriver setup complete.");

        // Define video output directory inside 'target/videos/'
        String videoDir = System.getProperty("user.dir") + "/target/videos/";
        Files.createDirectories(new File(videoDir).toPath()); // Ensure directory exists

        // Generate a video filename dynamically using class and method name
        String className = this.getClass().getSimpleName();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = className + "_" + dateFormat.format(new Date());

        // Full path of the video file
        videoFilePath = videoDir + fileName + ".avi";

        // Initialize screen recording
        screenRecorder = new CustomScreenRecorder(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(),
                new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()),
                new File(videoDir),
                fileName
        );
        screenRecorder.start();
        
        logger.info("🎥 Video recording started: {}", videoFilePath);
    }

    @Test
    void testGoogleTitle() throws IOException {
        logger.info("🌐 Opening Google homepage...");
        driver.get("https://www.google.com");

        // Get the page title
        String pageTitle = driver.getTitle();
        logger.info("📄 Page Title Retrieved: {}", pageTitle);

        // Validate title contains "Google"
        assertTrue(pageTitle.contains("Google"), "❌ Page title does not contain 'Google'!");

        // Take a screenshot and print its absolute path
        ScreenshotUtils.takeScreenshot(driver);

    }

    @AfterAll
    void teardown() throws Exception {
        if (driver != null) {
            driver.quit();
            logger.info("🛑 WebDriver closed.");
        }

        if (screenRecorder != null) {
            screenRecorder.stop();
            logger.info("🎬 Video recording stopped.");
            logger.info("📂 Video saved at: {}", videoFilePath);
            System.out.println("📂 Video saved at: " + videoFilePath);
        }
    }

  
}
