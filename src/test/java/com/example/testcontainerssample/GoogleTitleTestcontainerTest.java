package com.example.testcontainerssample;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.utils.ScreenshotUtils;

@Testcontainers
public class GoogleTitleTestcontainerTest {

	private static final Logger logger = LoggerFactory.getLogger(GoogleTitleTestcontainerTest.class);
	private static final DockerImageName CHROME_IMAGE = DockerImageName.parse("selenium/standalone-chrome:latest");

	@Container
	private static BrowserWebDriverContainer<?> chromeContainer = new BrowserWebDriverContainer<>(CHROME_IMAGE)
			.withCapabilities(new ChromeOptions());

	private WebDriver driver;

	@BeforeEach
	void setupDriver() {
		driver = new RemoteWebDriver(chromeContainer.getSeleniumAddress(), new ChromeOptions());
		driver.manage().window().maximize();
		logger.info("Selenium container started successfully.");
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

		// Take a screenshot (automatically names it using class & method)
		ScreenshotUtils.takeScreenshot(driver);
	}

	@AfterEach
	void teardown() {
		if (driver != null) {
			driver.quit();
			logger.info("WebDriver closed.");
		}
	}

}
