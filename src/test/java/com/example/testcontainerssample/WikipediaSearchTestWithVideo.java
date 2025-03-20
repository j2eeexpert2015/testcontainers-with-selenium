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
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.lifecycle.TestDescription;

import com.example.utils.ScreenshotUtils;

import software.xdev.testcontainers.selenium.containers.browser.BrowserWebDriverContainer;
import software.xdev.testcontainers.selenium.containers.browser.CapabilitiesBrowserWebDriverContainer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WikipediaSearchTestWithVideo {

    private static final Logger logger = LoggerFactory.getLogger(WikipediaSearchTestWithVideo.class);

    private CapabilitiesBrowserWebDriverContainer<?> browserContainer;
    private WebDriver driver;
    final Path recordingDir = Path.of("target/records");

    // Using Chrome
    private final Capabilities capabilities = new ChromeOptions();

    @BeforeAll
    void setup() throws MalformedURLException {
        logger.info("Starting Selenium Testcontainers...");

        // Ensure recording directory exists
        recordingDir.toFile().mkdirs();

        logger.info("Launching browser: {}", capabilities.getBrowserName());

        browserContainer = new CapabilitiesBrowserWebDriverContainer<>(capabilities)
                .withRecordingMode(BrowserWebDriverContainer.RecordingMode.RECORD_ALL)
                .withRecordingDirectory(recordingDir);

        browserContainer.start();

        driver = new RemoteWebDriver(browserContainer.getSeleniumAddressURI().toURL(), capabilities, false);

        driver.manage().window().maximize();
        logger.info("Selenium container started successfully for {}", capabilities.getBrowserName());
    }

    @Test
    void testWikipediaSearch() throws IOException {
        logger.info("Navigating to Wikipedia homepage...");
        driver.get("https://www.wikipedia.org/");

        // Find the search input field
        WebElement searchInput = driver.findElement(By.id("searchInput"));
        searchInput.sendKeys("Selenium (software)");  // Type the search term

        // Press ENTER to search
        searchInput.sendKeys(Keys.RETURN);
        logger.info("Performed Wikipedia search for 'Selenium (software)'");

        // Wait for the results page to load
        try {
            Thread.sleep(2000); // Wait for search results
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify the title contains the search term
        String pageTitle = driver.getTitle();
        logger.info("📄 Page Title Retrieved: {}", pageTitle);
        assertTrue(pageTitle.contains("Selenium"), "Page title does not contain 'Selenium'!");

        // Take a screenshot of the search results page
        ScreenshotUtils.takeScreenshot(driver);
    }

    @AfterAll
    void teardown() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed.");
        }

        if (browserContainer != null) {
            // Ensure video is finalized using `afterTest()`
            browserContainer.afterTest(new TestDescription() {
                @Override
                public String getTestId() {
                    return "wikipedia-search-test";
                }

                @Override
                public String getFilesystemFriendlyName() {
                    return "wikipedia-search-test";
                }
            }, Optional.empty());

            browserContainer.stop();
            logger.info("🎬 Video recording stopped.");

            // Ensure video file path is logged
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


}
