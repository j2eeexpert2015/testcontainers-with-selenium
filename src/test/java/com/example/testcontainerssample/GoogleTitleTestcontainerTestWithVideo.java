package com.example.testcontainerssample;

import static com.example.utils.FileUtils.findLatestVideoFile;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.utils.ScreenshotUtils;

import software.xdev.testcontainers.selenium.containers.browser.BrowserWebDriverContainer;
import software.xdev.testcontainers.selenium.containers.browser.CapabilitiesBrowserWebDriverContainer;

@Testcontainers
public class GoogleTitleTestcontainerTestWithVideo {

    private static final Logger logger = LoggerFactory.getLogger(GoogleTitleTestcontainerTestWithVideo.class);

    private static WebDriver driver;
    private static Path recordingDir = Path.of("target/records");
    private static Capabilities capabilities = new ChromeOptions();
    @Container
    private static CapabilitiesBrowserWebDriverContainer<?> browserContainer =browserContainer = new CapabilitiesBrowserWebDriverContainer<>(capabilities)
            .withRecordingMode(BrowserWebDriverContainer.RecordingMode.RECORD_ALL)
            .withRecordingDirectory(recordingDir);

    @BeforeAll
    static void setup() throws MalformedURLException {
        driver = new RemoteWebDriver(browserContainer.getSeleniumAddressURI().toURL(), capabilities, false);
        driver.manage().window().maximize();
        logger.info("Selenium container started successfully for {}", capabilities.getBrowserName());
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

        // Take a screenshot
        ScreenshotUtils.takeScreenshot(driver);
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed.");
        }
        
        Optional<File> videoFile = findLatestVideoFile(recordingDir);
        videoFile.ifPresentOrElse(
            file -> {
                logger.info("Video saved at: {}", file.getAbsolutePath());
                System.out.println("Video saved at: " + file.getAbsolutePath());
            },
            () -> logger.warn("No video file found in directory: {}", recordingDir.toAbsolutePath())
        );
    }

}
