package com.example.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);

    public static String takeScreenshot(WebDriver driver) throws IOException {
        // Get the class and method name dynamically
        String className = Thread.currentThread().getStackTrace()[2].getClassName().substring(Thread.currentThread().getStackTrace()[2].getClassName().lastIndexOf('.') + 1);
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
